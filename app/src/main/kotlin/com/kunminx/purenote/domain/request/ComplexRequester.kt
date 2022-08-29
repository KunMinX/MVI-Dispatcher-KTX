package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.event.ComplexEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Create by KunMinX at 2022/7/5
 */
class ComplexRequester : MviDispatcherKTX<ComplexEvent>() {

  private val _interval: Flow<Int> = interval(1000)

  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 & mutableSharedFlow.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 高频痛点。
   *  ~
   *  ~
   *  As the 'only credible source', it receives messages sent from the page,
   *  processes the business logic internally, and distributes them through sendResult results.
   *  ~
   *  At the same time, as the adult stage of Single Source of Truth,
   *  automatically eliminates the high-frequency pain spots of "mutable boilerplate code
   *  & mutableSharedFlow.setValue abuse & repeatOnLifecycle + SharedFlow miss result".
   */
  override suspend fun onHandle(event: ComplexEvent) {
    when (event) {
      //TODO tip 2: 定长队列，随取随用，绝不丢失事件
      // 此处通过 Flow 轮询模拟事件连发，可于 Logcat Debug 见输出
      // ~
      // Fixed length queue, on demand, never lose events
      // Here, Flow polling simulation events are sent repeatedly, and the output can be seen in logcat debug

      is ComplexEvent.ResultTest1 -> _interval.collect { input(ComplexEvent.ResultTest4(it)) }
      is ComplexEvent.ResultTest2 -> timer(1000).collect { sendResult(event) }
      is ComplexEvent.ResultTest3 -> sendResult(event)
      is ComplexEvent.ResultTest4 -> sendResult(event)
    }
  }

  private fun interval(duration: Long) = flow {
    for (i in 0..Int.MAX_VALUE) {
      delay(duration)
      emit(i)
    }
  }

  private fun timer(duration: Long) = flow {
    delay(duration)
    emit(true)
  }
}
