package com.kunminx.purenote.ui.page

import android.util.Log
import androidx.activity.viewModels
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.purenote.R
import com.kunminx.purenote.domain.event.ComplexEvent
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.ComplexRequester

class MainActivity : BaseActivity() {
  private val complexRequester by viewModels<ComplexRequester>()
  private val messenger by viewModels<PageMessenger>()

  override fun onInitView() {
    setContentView(R.layout.activity_main)
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    messenger.output(this) { messages ->
      if (messages.eventId == Messages.EVENT_FINISH_ACTIVITY) finish()
    }

    complexRequester.output(this) { complexEvent ->
      when (complexEvent.eventId) {
        ComplexEvent.EVENT_TEST_1 -> Log.d("complexEvent", "---1")
        ComplexEvent.EVENT_TEST_2 -> Log.d("complexEvent", "---2")
        ComplexEvent.EVENT_TEST_3 -> Log.d("complexEvent", "---3")
        ComplexEvent.EVENT_TEST_4 -> Log.d("complexEvent", "---4 " + complexEvent.result!!.count)
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    super.onInput()

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_1))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_2))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
    complexRequester.input(ComplexEvent(ComplexEvent.EVENT_TEST_3))
  }
}