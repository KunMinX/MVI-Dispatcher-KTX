package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/7/3
 */
open class MviDispatcherKTX<E> : ViewModel() {
  private var _sharedFlow: MutableSharedFlow<E>? = null
  private val delayMap: MutableMap<Int, Boolean> = mutableMapOf()

  private fun initQueue() {
    if (_sharedFlow == null) _sharedFlow = MutableSharedFlow(
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
      extraBufferCapacity = initQueueMaxLength()
    )
  }

  protected open fun initQueueMaxLength(): Int {
    return DEFAULT_QUEUE_LENGTH
  }

  fun output(activity: AppCompatActivity?, observer: (E) -> Unit) {
    initQueue()
    delayMap[System.identityHashCode(activity)] = true
    activity?.lifecycleScope?.launch {
      activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
        delayMap.remove(System.identityHashCode(activity))
        _sharedFlow?.collect { observer.invoke(it) }
      }
    }
  }

  fun output(fragment: Fragment?, observer: (E) -> Unit) {
    initQueue()
    delayMap[System.identityHashCode(fragment)] = true
    fragment?.viewLifecycleOwner?.lifecycleScope?.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        delayMap.remove(System.identityHashCode(fragment))
        _sharedFlow?.collect { observer.invoke(it) }
      }
    }
  }

  protected suspend fun sendResult(event: E) {
    _sharedFlow?.emit(event)
  }

  fun input(event: E) {
    viewModelScope.launch {
      if (needDelayForLifecycleState) delayForLifecycleState().collect { onHandle(event) }
      else onHandle(event)
    }
  }

  private val needDelayForLifecycleState
    get() = delayMap.isNotEmpty()

  protected open suspend fun onHandle(event: E) {}

  private fun delayForLifecycleState() = flow {
    delay(1)
    emit(true)
  }

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
  }
}