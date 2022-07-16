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
   *  自动消除 “mutable 样板代码 + mutableSharedFlow.emit 误用滥用” 高频痛点。
   */
  override fun input(event: NoteEvent) {
    viewModelScope.launch {
      when (event) {
        is NoteEvent.MarkItem -> sendResult(event.copy(DataRepository.instance.updateNote(event.note!!)))
        is NoteEvent.UpdateItem -> sendResult(event.copy(DataRepository.instance.updateNote(event.note!!)))
        is NoteEvent.AddItem -> sendResult(event.copy(DataRepository.instance.insertNote(event.note!!)))
        is NoteEvent.RemoveItem -> sendResult(event.copy(DataRepository.instance.deleteNote(event.note!!)))
        is NoteEvent.GetNoteList -> {
          DataRepository.instance.getNotes().collect { sendResult(event.copy(it)) }
        }
        is NoteEvent.ToppingItem -> {
          val success = DataRepository.instance.updateNote(event.note!!)
          if (success) DataRepository.instance.getNotes().collect {
            sendResult(NoteEvent.GetNoteList(it))
          }
        }
      }
    }
  }
}