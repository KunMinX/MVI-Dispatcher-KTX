package com.kunminx.architecture.domain.event

import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.ui.scope.ApplicationInstance
import com.kunminx.architecture.domain.queue.FixedLengthList.QueueCallback
import com.kunminx.architecture.domain.queue.FixedLengthList
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.dispatch.MviDispatcher

/**
 * Create by KunMinX at 2022/6/16
 */
open class Event<P, R> {
  var eventId = 0
  var param: P? = null
  var result: R? = null
}