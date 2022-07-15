package com.kunminx.purenote.domain.event

import com.kunminx.architecture.domain.event.Event

/**
 * Create by KunMinX at 2022/6/16
 */
class ComplexEvent(eventId: Int) : Event<ComplexEvent.Param?, ComplexEvent.Result?>() {
  class Param {
    var count: Long = 0
  }

  class Result {
    var count: Long = 0
  }

  companion object {
    const val EVENT_TEST_1 = 1
    const val EVENT_TEST_2 = 2
    const val EVENT_TEST_3 = 3
    const val EVENT_TEST_4 = 4
  }

  init {
    this.eventId = eventId
    param = Param()
    result = Result()
  }
}