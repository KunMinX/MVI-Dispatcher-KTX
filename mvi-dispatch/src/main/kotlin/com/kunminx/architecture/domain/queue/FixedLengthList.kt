package com.kunminx.architecture.domain.queue

import java.util.*

/**
 * Create by KunMinX at 2022/7/5
 */
class FixedLengthList<T> : LinkedList<T>() {
  private var maxLength = 0
  private var hasBeenInit = false
  private var callbackToRemoveFirst: ((T) -> Unit)? = null
  fun init(maxLength: Int, action: (t: T) -> Unit) {
    if (!hasBeenInit) {
      this.maxLength = maxLength
      this.callbackToRemoveFirst = action
      hasBeenInit = true
    }
  }

  override fun add(element: T): Boolean {
    if (size + 1 > maxLength) {
      val t1 = super.removeFirst()
      if (callbackToRemoveFirst != null) callbackToRemoveFirst!!.invoke(t1)
    }
    return super.add(element)
  }
}