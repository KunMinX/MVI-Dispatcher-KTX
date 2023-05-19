package com.kunminx.purenote.domain.message

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.domain.intent.Messages

/**
 * Create by KunMinX at 2022/6/14
 */
class PageMessenger : MviDispatcherKTX<Messages>() {
  /**
   * TODO tip 1：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   *  消除 “mutable 样板代码 & mutable.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 等高频痛点。
   */
  override suspend fun onHandle(event: Messages) {
    sendResult(event)

    // TODO：tip 2：除接收来自 Activity/Fragment 的事件，亦可从 Dispatcher 内部发送事件（作为副作用）：
    //  ~
    //  if (sent from within) {
    //    Messages msg = new Messages(Messages.EVENT_SHOW_DIALOG);
    //    sendResult(msg);
    //  }
  }
}
