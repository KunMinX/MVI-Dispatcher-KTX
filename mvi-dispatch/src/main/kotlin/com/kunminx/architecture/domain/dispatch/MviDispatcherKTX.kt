package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
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
  private var lastValue = LastValue(0)
  private val _sharedFlow: MutableSharedFlow<E>? by lazy {
    MutableSharedFlow(
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
      extraBufferCapacity = initQueueMaxLength(),
      replay = initQueueMaxLength()
    )
  }

  protected open fun initQueueMaxLength(): Int {
    return DEFAULT_QUEUE_LENGTH
  }

  fun output(activity: AppCompatActivity?, observer: (E) -> Unit) {
    activity?.lifecycleScope?.launch {
      activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.flowConsumeOnce()?.collect { observer.invoke(it) }
      }
    }
  }

  fun output(fragment: Fragment?, observer: (E) -> Unit) {
    fragment?.viewLifecycleOwner?.lifecycleScope?.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.flowConsumeOnce()?.collect { observer.invoke(it) }
      }
    }
  }

  protected suspend fun sendResult(event: E) {
    _sharedFlow?.emit(event)
  }

  fun input(event: E) {
    viewModelScope.launch { onHandle(event) }
  }

  protected open suspend fun onHandle(event: E) {}

  private fun delayForLifecycleState() = flow {
    delay(1)
    emit(true)
  }

  private fun <E> Flow<E>.flowConsumeOnce(): Flow<E> = callbackFlow {
    this@flowConsumeOnce.collect {
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
