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
  override suspend fun onHandle(event: Messages) {
    sendResult(event)

    // TODO：tip 2：除接收 Activity/Fragment 事件，亦可从 Dispatcher 内部发送事件（作为副作用）：
    //  ~
    //  In addition to receiving events from Activity/Fragment,
    //  events can also be sent from within the Dispatcher (as a side effect)
    //  ~
    //  if (sent from within) {
    //    Messages msg = new Messages(Messages.EVENT_SHOW_DIALOG);
    //    sendResult(msg);
    //  }
  }
}
