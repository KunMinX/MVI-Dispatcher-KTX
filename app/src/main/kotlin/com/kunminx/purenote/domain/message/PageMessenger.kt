package com.kunminx.purenote.domain.message

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.event.Messages

/**
 * Create by KunMinX at 2022/6/14
 */
class PageMessenger : MviDispatcherKTX<Messages>() {
  /**
   * TODO tip 1：
   *  作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  ~
   *  与此同时，作为唯一可信源成熟态，
   *  自动消除 “mutable 样板代码 + mutableSharedFlow.emit 误用滥用” 高频痛点。
   */
  override suspend fun onInput(event: Messages) {
    sendResult(event)

    // TODO：tip 2：除接收 Activity/Fragment 事件，亦可从 Dispatcher 内部发送事件（作为副作用）：
    //  if (此处欲内部推送) {
    //    val msg = Messages(Messages.ShowDialog);
    //    sendResult(msg);
    //  }
  }
}