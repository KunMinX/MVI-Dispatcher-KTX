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
open class MviDispatcherKTX<T : Any> : ViewModel(), DefaultLifecycleObserver {
  private var version = START_VERSION
  private var currentVersion = START_VERSION
  private var observerCount = 0
  private val _sharedFlow: MutableSharedFlow<ConsumeOnceValue<T>> by lazy {
    MutableSharedFlow(
      onBufferOverflow = BufferOverflow.DROP_OLDEST,
      extraBufferCapacity = initQueueMaxLength(),
      replay = initQueueMaxLength()
    )
  }

  protected open fun initQueueMaxLength(): Int {
    return DEFAULT_QUEUE_LENGTH
  }

  fun output(activity: AppCompatActivity?, observer: (T) -> Unit) {
    outputTo(activity, observer)
  }

  fun output(fragment: Fragment?, observer: (T) -> Unit) {
    outputTo(fragment?.viewLifecycleOwner, observer)
  }

  private fun outputTo(lifecycleOwner: LifecycleOwner?, observer: (T) -> Unit) {
    currentVersion = version
    observerCount++
    lifecycleOwner?.lifecycle?.addObserver(this)
    lifecycleOwner?.lifecycleScope?.launch {
      lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        _sharedFlow.collect {
          if (version > currentVersion) {
            if (it.consumeCount >= observerCount) return@collect
            it.consumeCount++
            observer(it.value)
          }
        }
      }
    }
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    observerCount--
  }

  protected suspend fun sendResult(intent: T) {
    version++
    _sharedFlow.emit(ConsumeOnceValue(value = intent))
  }

  fun input(intent: T) {
    viewModelScope.launch { onHandle(intent) }
  }

  protected open suspend fun onHandle(intent: T) {}

  private data class ConsumeOnceValue<T>(
    var consumeCount: Int = 0,
    val value: T
  )

  companion object {
    private const val DEFAULT_QUEUE_LENGTH = 10
    private const val START_VERSION = -1
  }
}
