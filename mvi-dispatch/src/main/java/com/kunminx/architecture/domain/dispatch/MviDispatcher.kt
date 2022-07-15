package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.kunminx.architecture.domain.event.Event
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.queue.FixedLengthList

/**
 * Create by KunMinX at 2022/7/3
 */
open class MviDispatcher<E : Event<*, *>?> : ViewModel(), DefaultLifecycleObserver {
  private val mOwner = HashMap<Int, LifecycleOwner>()
  private val mFragmentOwner = HashMap<Int, LifecycleOwner>()
  private val mObservers = HashMap<Int, Observer<E>>()
  private val mResults = FixedLengthList<MutableResult<E>>()
  protected open fun initQueueMaxLength(): Int {
    return DEFAULT_QUEUE_LENGTH
  }

  fun output(activity: AppCompatActivity, observer: Observer<E>) {
    activity.lifecycle.addObserver(this)
    val identityId = System.identityHashCode(activity)
    outputTo(identityId, activity, observer)
  }

  fun output(fragment: Fragment, observer: Observer<E>) {
    fragment.lifecycle.addObserver(this)
    val identityId = System.identityHashCode(fragment)
    mFragmentOwner[identityId] = fragment
    outputTo(identityId, fragment.viewLifecycleOwner, observer)
  }

  private fun outputTo(identityId: Int, owner: LifecycleOwner, observer: Observer<E>) {
    mOwner[identityId] = owner
    mObservers[identityId] = observer
    for (result in mResults) {
      result.observe(owner, observer)
    }
  }

  protected fun sendResult(event: E) {
    mResults.init(initQueueMaxLength()) { mutableResult ->
      for ((_, observer) in mObservers) {
        mutableResult.removeObserver(observer)
      }
    }
    var eventExist = false
    for (result in mResults) {
      val id1 = System.identityHashCode(result.value)
      val id2 = System.identityHashCode(event)
      if (id1 == id2) {
        eventExist = true
        break
      }
    }
    if (!eventExist) {
      val result = MutableResult(event)
      for ((key, observer) in mObservers) {
        val owner = mOwner[key]!!
        result.observe(owner, observer)
      }
      mResults.add(result)
    }
    var result: MutableResult<E>? = null
    for (r in mResults) {
      val id1 = System.identityHashCode(r.value)
      val id2 = System.identityHashCode(event)
      if (id1 == id2) {
        result = r
        break
      }
    }
    if (result != null) result.value = event
  }

  open fun input(event: E) {}
  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    val isFragment = owner is Fragment
    for ((key, owner1) in if (isFragment) mFragmentOwner.entries else mOwner.entries) {
      if (owner1 == owner) {
        mOwner.remove(key)
        if (isFragment) mFragmentOwner.remove(key)
        for (mutableResult in mResults) {
          mObservers.get(key)?.let { mutableResult.removeObserver(it) }
        }
        mObservers.remove(key)
        break
      }
    }
    if (mObservers.size == 0) mResults.clear()
  }

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
  }
}