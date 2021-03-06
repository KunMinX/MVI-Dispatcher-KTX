package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.event.NoteEvent

/**
 * Create by KunMinX at 2022/6/14
 */
class NoteRequester : MviDispatcherKTX<NoteEvent>() {
  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 & mutableSharedFlow.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 高频痛点。
   *  ~
   *  ~
   *  As the 'only credible source', it receives messages sent from the page,
   *  processes the business logic internally, and distributes them through sendResult results.
   *  ~
   *  At the same time, as the adult stage of Single Source of Truth,
   *  automatically eliminates the high-frequency pain spots of "mutable boilerplate code
   *  & mutableSharedFlow.setValue abuse & repeatOnLifecycle + SharedFlow miss result".
   */
  override suspend fun onHandle(event: NoteEvent) {
    when (event) {
      is NoteEvent.MarkItem -> sendResult(event.copy(DataRepository.updateNote(event.note!!)))
      is NoteEvent.UpdateItem -> sendResult(event.copy(DataRepository.updateNote(event.note!!)))
      is NoteEvent.AddItem -> sendResult(event.copy(DataRepository.insertNote(event.note!!)))
      is NoteEvent.RemoveItem -> sendResult(event.copy(DataRepository.deleteNote(event.note!!)))
      is NoteEvent.GetNoteList -> {
        DataRepository.getNotes().collect { sendResult(event.copy(it)) }
      }
      is NoteEvent.ToppingItem -> {
        val success = DataRepository.updateNote(event.note!!)
        if (success) DataRepository.getNotes().collect {
          sendResult(NoteEvent.GetNoteList(it))
        }
      }
    }
  }
}
