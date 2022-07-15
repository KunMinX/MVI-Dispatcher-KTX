package com.kunminx.purenote.domain.event

import com.kunminx.architecture.domain.event.Event

/**
 * Create by KunMinX at 2022/6/16
 */
class Messages(eventId: Int) : Event<Messages.Param, Messages.Result>() {
  class Param
  class Result
  companion object {
    const val EVENT_REFRESH_NOTE_LIST = 1
    const val EVENT_FINISH_ACTIVITY = 2
  }

  init {
    this.eventId = eventId
    param = Param()
    result = Result()
  }
}