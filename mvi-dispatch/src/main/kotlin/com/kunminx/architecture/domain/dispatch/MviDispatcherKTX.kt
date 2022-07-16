package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/7/3
 */
open class MviDispatcherKTX<E> : ViewModel() {
  private var _sharedFlow: MutableSharedFlow<E>? = null

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
    activity?.lifecycleScope?.launch {
      activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.collect { observer.invoke(it) }
      }
    }
  }

  fun output(fragment: Fragment?, observer: (E) -> Unit) {
    initQueue()
    fragment?.viewLifecycleOwner?.lifecycleScope?.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.collect { observer.invoke(it) }
      }
    }
  }

  protected suspend fun sendResult(event: E) {
    _sharedFlow?.emit(event)
  }

  open fun input(event: E) {

  }

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
  }
}