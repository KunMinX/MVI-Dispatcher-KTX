package com.kunminx.purenote.domain.request

import com.kunminx.architecture.data.response.DataResult
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.event.NoteEvent

/**
 * Create by KunMinX at 2022/6/14
 */
class NoteRequester : MviDispatcher<NoteEvent>() {
  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  override fun input(event: NoteEvent) {
    when (event.eventId) {
      NoteEvent.EVENT_GET_NOTE_LIST -> DataRepository.instance.getNotes { dataResult ->
        event.result!!.notes = dataResult.result
        sendResult(event)
      }
      NoteEvent.EVENT_UPDATE_ITEM, NoteEvent.EVENT_MARK_ITEM ->
        event.param?.note?.let {
          DataRepository.instance.updateNote(it) { dataResult ->
            event.result!!.isSuccess = dataResult.result
            sendResult(event)
          }
        }
      NoteEvent.EVENT_TOPPING_ITEM -> event.param?.note?.let {
        DataRepository.instance.updateNote(it) { dataResult ->
          event.result!!.isSuccess = dataResult.result
          if (event.result!!.isSuccess) {
            DataRepository.instance
              .getNotes { dataResult1: DataResult<MutableList<Note>> ->
                event.result!!.notes = dataResult1.result
                sendResult(event)
              }
          }
        }
      }
      NoteEvent.EVENT_ADD_ITEM -> event.param!!.note?.let {
        DataRepository.instance.insertNote(it) { dataResult ->
          event.result!!.isSuccess = dataResult.result
          sendResult(event)
        }
      }
      NoteEvent.EVENT_REMOVE_ITEM -> event.param!!.note?.let {
        DataRepository.instance.deleteNote(it) { dataResult ->
          event.result!!.isSuccess = dataResult.result
          sendResult(event)
        }
      }
    }
  }
}