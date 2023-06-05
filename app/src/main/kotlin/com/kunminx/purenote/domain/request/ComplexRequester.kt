package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.intent.ComplexIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Create by KunMinX at 2022/7/5
 */
class ComplexRequester : MviDispatcherKTX<ComplexIntent>() {

  private val _interval: Flow<Int> = interval(1000)

  /**
   * TODO tip 1：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   *  消除 “mutable 样板代码 & mutable.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 等高频痛点。
   */
  override suspend fun onHandle(intent: ComplexIntent) {
    when (intent) {
      //TODO tip 2: 定长队列，随取随用，绝不丢失事件
      // 此处通过 Flow 轮询模拟事件连发，可于 Logcat Debug 见输出

      is ComplexIntent.ResultTest1 -> _interval.collect { input(ComplexIntent.ResultTest4(it)) }
      is ComplexIntent.ResultTest2 -> timer(1000).collect { sendResult(intent) }
      is ComplexIntent.ResultTest3 -> sendResult(intent)
      is ComplexIntent.ResultTest4 -> sendResult(intent)
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
