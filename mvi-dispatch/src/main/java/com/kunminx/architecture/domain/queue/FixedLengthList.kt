package com.kunminx.architecture.domain.queue

import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.ui.scope.ApplicationInstance
import com.kunminx.architecture.domain.queue.FixedLengthList.QueueCallback
import com.kunminx.architecture.domain.queue.FixedLengthList
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import java.util.*

/**
 * Create by KunMinX at 2022/7/5
 */
class FixedLengthList<T> : LinkedList<T>() {
  private var maxLength = 0
  private var hasBeenInit = false
  private var queueCallback: QueueCallback<T>? = null
  fun init(maxLength: Int, queueCallback: QueueCallback<T>?) {
    if (!hasBeenInit) {
      this.maxLength = maxLength
      this.queueCallback = queueCallback
      hasBeenInit = true
    }
  }

  override fun add(t: T): Boolean {
    if (size + 1 > maxLength) {
      val t1 = super.removeFirst()
      if (queueCallback != null) queueCallback!!.onRemoveFirst(t1)
    }
    return super.add(t)
  }

  interface QueueCallback<T> {
    fun onRemoveFirst(t: T)
  }
}