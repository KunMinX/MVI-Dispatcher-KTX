package com.kunminx.purenote.ui.page

import android.util.Log
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.purenote.R
import com.kunminx.purenote.domain.event.ComplexEvent
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.ComplexRequester

class MainActivity : BaseActivity() {
  private var mMessenger: PageMessenger? = null
  private var mComplexRequester: ComplexRequester? = null
  override fun onInitViewModel() {
    mMessenger = getApplicationScopeViewModel(PageMessenger::class.java)
    mComplexRequester = getActivityScopeViewModel(ComplexRequester::class.java)
  }

  override fun onInitView() {
    setContentView(R.layout.activity_main)
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    mMessenger?.output(this) { messages ->
      if (messages.eventId == Messages.EVENT_FINISH_ACTIVITY) finish()
    }

    mComplexRequester?.output(this) { complexEvent ->
      if (complexEvent.eventId == ComplexEvent.EVENT_TEST_1) Log.d(
        "complexEvent",
        "---1"
      ) else if (complexEvent.eventId == ComplexEvent.EVENT_TEST_2) Log.d(
        "complexEvent",
        "---2"
      ) else if (complexEvent.eventId == ComplexEvent.EVENT_TEST_3) Log.d(
        "complexEvent",
        "---3"
      ) else if (complexEvent.eventId == ComplexEvent.EVENT_TEST_4) Log.d(
        "complexEvent",
        "---4 " + complexEvent.result!!.count
      )
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    super.onInput()

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_1))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
  }
}