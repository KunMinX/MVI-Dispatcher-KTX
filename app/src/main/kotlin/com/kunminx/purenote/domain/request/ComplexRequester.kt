package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.event.ComplexEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * Create by KunMinX at 2022/7/5
 */
class ComplexRequester : MviDispatcherKTX<ComplexEvent>() {
  /**
   * TODO tip 2：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + mutableSharedFlow.emit 误用滥用” 高频痛点。
   */
  override suspend fun onInput(event: ComplexEvent) {
    when (event) {
      //TODO tip 3: 定长队列，随取随用，绝不丢失事件
      // 此处通过 Flow 轮询模拟事件连发，可于 Logcat Debug 见输出

      is ComplexEvent.ResultTest1 -> interval().collect { input(ComplexEvent.ResultTest4(it)) }
      is ComplexEvent.ResultTest2 -> timer(1000).collect { sendResult(event) }
      is ComplexEvent.ResultTest3 -> sendResult(event)
      is ComplexEvent.ResultTest4 -> sendResult(event)
    }
  }

  private fun interval() = flow {
    for (i in 0..Int.MAX_VALUE) {
      delay(100)
      emit(i)
    }
  }

  private fun timer(duration: Long) = flow {
    delay(duration)
    emit(true)
  }
}