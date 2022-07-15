package com.kunminx.purenote.domain.request

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.event.ComplexEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Create by KunMinX at 2022/7/5
 */
class ComplexRequester : MviDispatcherKTX<ComplexEvent>() {
  /**
   * TODO tip 2：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  @SuppressLint("CheckResult")
  override fun input(event: ComplexEvent) {
    viewModelScope.launch {
      when (event) {
        //TODO tip 3: 定长队列，随取随用，绝不丢失事件
        // 此处通过 RxJava 轮询模拟事件连发，可于 Logcat Debug 见输出

        is ComplexEvent.ResultTest1 -> interval().collect {
          val event1 = ComplexEvent.ResultTest4(it)
          input(event1)
        }
        is ComplexEvent.ResultTest2 -> timer().collect { sendResult(event) }
        is ComplexEvent.ResultTest3 -> sendResult(event)
        is ComplexEvent.ResultTest4 -> sendResult(event)
      }
    }
  }

  private fun interval() = flow {
    for (i in 1..Int.MAX_VALUE) {
      delay(1)
      emit(i)
    }
  }

  private fun timer() = flow {
    delay(200)
    emit(true)
  }
}