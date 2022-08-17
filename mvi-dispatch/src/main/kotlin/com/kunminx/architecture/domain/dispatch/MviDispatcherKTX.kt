package com.kunminx.architecture.domain.dispatch

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/7/3
 */
open class MviDispatcherKTX<E> : ViewModel(), DefaultLifecycleObserver {
  private var version = START_VERSION
  private var currentVersion = START_VERSION
  private var observerCount = 0
  private val _sharedFlow: MutableSharedFlow<ConsumeOnceValue<E>>? by lazy {
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
    currentVersion = version
    observerCount++
    activity?.lifecycle?.addObserver(this)
    activity?.lifecycleScope?.launch {
      activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.collect {
          if (version > currentVersion) {
            if (it.consumeCount >= observerCount) return@collect
            it.consumeCount++
            observer.invoke(it.value)
          }
        }
      }
    }
  }

  fun output(fragment: Fragment?, observer: (E) -> Unit) {
    currentVersion = version
    observerCount++
    fragment?.viewLifecycleOwner?.lifecycle?.addObserver(this)
    fragment?.viewLifecycleOwner?.lifecycleScope?.launch {
      fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow?.collect {
          if (version > currentVersion) {
            if (it.consumeCount >= observerCount) return@collect
            it.consumeCount++
            observer.invoke(it.value)
          }
        }
      }
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    observerCount--
  }

  protected suspend fun sendResult(event: E) {
    version++
    _sharedFlow?.emit(ConsumeOnceValue(value = event))
  }

  fun input(event: E) {
    viewModelScope.launch { onHandle(event) }
  }

  protected open suspend fun onHandle(event: E) {}

  data class ConsumeOnceValue<E>(
    var consumeCount: Int = 0,
    val value: E
  )

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
    private const val START_VERSION = -1
  }
}
