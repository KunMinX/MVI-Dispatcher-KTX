package com.kunminx.purenote.domain.request

import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.event.NoteEvent
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/6/14
 */
class NoteRequester : MviDispatcherKTX<NoteEvent>() {
  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  override fun input(event: NoteEvent) {
    viewModelScope.launch {
      when (event) {
        is NoteEvent.GetNoteList -> {
          sendResult(NoteEvent.GetNoteList(DataRepository.instance.getNotes()))
        }
        is NoteEvent.UpdateItem, is NoteEvent.MarkItem -> {
          val success = DataRepository.instance.updateNote(event.note!!)
          if (success) sendResult(NoteEvent.UpdateItem(success))
        }
        is NoteEvent.ToppingItem -> {
          val success = DataRepository.instance.updateNote(event.note!!)
          if (success) sendResult(NoteEvent.GetNoteList(DataRepository.instance.getNotes()))
        }
        is NoteEvent.AddItem -> {
          val success = DataRepository.instance.insertNote(event.note!!)
          if (success) sendResult(NoteEvent.AddItem(success))
        }
        is NoteEvent.RemoveItem -> {
          val success = DataRepository.instance.deleteNote(event.note!!)
          if (success) sendResult(NoteEvent.RemoveItem(success))
        }
      }
    }
  }
}