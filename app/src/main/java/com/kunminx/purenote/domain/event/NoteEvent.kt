package com.kunminx.purenote.domain.event

import com.kunminx.architecture.domain.event.Event
import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/16
 */
class NoteEvent(eventId: Int) : Event<NoteEvent.Param?, NoteEvent.Result?>() {
  fun setNote(note: Note?): NoteEvent {
    param!!.note = note
    return this
  }

  class Param {
    var note: Note? = null
  }

  class Result {
    var notes: List<Note>? = null
    var isSuccess = false
  }

  companion object {
    const val EVENT_GET_NOTE_LIST = 1
    const val EVENT_REMOVE_ITEM = 2
    const val EVENT_UPDATE_ITEM = 3
    const val EVENT_MARK_ITEM = 4
    const val EVENT_TOPPING_ITEM = 5
    const val EVENT_ADD_ITEM = 6
  }

  init {
    this.eventId = eventId
    param = Param()
    result = Result()
  }
}