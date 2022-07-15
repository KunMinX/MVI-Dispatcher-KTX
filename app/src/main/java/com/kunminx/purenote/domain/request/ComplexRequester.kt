package com.kunminx.purenote.domain.request

import android.annotation.SuppressLint
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import com.kunminx.purenote.domain.event.ComplexEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Create by KunMinX at 2022/7/5
 */
class ComplexRequester : MviDispatcher<ComplexEvent>() {
  /**
   * TODO tip 1：可初始化配置队列长度，自动丢弃队首过时消息
   */
  override fun initQueueMaxLength(): Int {
    return 5
  }

  /**
   * TODO tip 2：
   * 作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   * ~
   * 与此同时，作为唯一可信源成熟态，
   * 自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  @SuppressLint("CheckResult")
  override fun input(event: ComplexEvent) {
    super.input(event)
    when (event.eventId) {
      ComplexEvent.EVENT_TEST_1 ->
        //TODO tip 3: 定长队列，随取随用，绝不丢失事件
        // 此处通过 RxJava 轮询模拟事件连发，可于 Logcat Debug 见输出
        Observable.interval(1, TimeUnit.MILLISECONDS)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe { aLong: Long ->
            val event1 = ComplexEvent(ComplexEvent.EVENT_TEST_4)
            event1.param!!.count = aLong
            input(event1)
          }
      ComplexEvent.EVENT_TEST_2 -> Observable.timer(200, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { aLong: Long? -> sendResult(event) }
      ComplexEvent.EVENT_TEST_3 -> sendResult(event)
      ComplexEvent.EVENT_TEST_4 -> {
        event.result!!.count = event.param!!.count
        sendResult(event)
      }
    }
  }
}