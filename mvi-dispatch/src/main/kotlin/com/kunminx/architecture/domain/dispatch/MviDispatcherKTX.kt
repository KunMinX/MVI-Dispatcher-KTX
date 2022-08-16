package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlin.collections.set
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/7/3
 */
open class MviDispatcherKTX<E> : ViewModel() {
  private var _sharedFlow: MutableSharedFlow<E>? = null
  private val delayMap: MutableMap<Int, Boolean> = mutableMapOf()
  private var lastValue = LastValue(0)

  private fun initQueue() {
    if (_sharedFlow == null) _sharedFlow = MutableSharedFlow(
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
      extraBufferCapacity = initQueueMaxLength(),
      replay = 1
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
        _sharedFlow?.flowOnLifecycleConsumeOnce()?.collect { observer.invoke(it) }
      }
    }
  }

  fun output(fragment: Fragment?, observer: (E) -> Unit) {
    initQueue()
    delayMap[System.identityHashCode(fragment)] = true
    fragment?.viewLifecycleOwner?.lifecycleScope?.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        delayMap.remove(System.identityHashCode(fragment))
        _sharedFlow?.flowOnLifecycleConsumeOnce()?.collect { observer.invoke(it) }
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

  private fun <E> Flow<E>.flowOnLifecycleConsumeOnce(): Flow<E> = callbackFlow {
    this@flowOnLifecycleConsumeOnce.collect {
      val newHashCode = System.identityHashCode(it)
      if (lastValue.hashCode != newHashCode) send(it)
      lastValue.hashCode = newHashCode
    }
    lastValue.hashCode = 0
    close()
  }

  data class LastValue(var hashCode: Int)

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
  }
}
