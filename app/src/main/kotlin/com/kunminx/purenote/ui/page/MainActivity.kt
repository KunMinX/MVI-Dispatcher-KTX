package com.kunminx.purenote.ui.page

import android.util.Log
import androidx.activity.viewModels
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.page.StateHolder
import com.kunminx.purenote.BR
import com.kunminx.purenote.R
import com.kunminx.purenote.domain.intent.ComplexIntent
import com.kunminx.purenote.domain.intent.Messages
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.ComplexRequester

class MainActivity : BaseActivity() {
  private val states by viewModels<MainStates>()
  private val messenger by viewModels<PageMessenger>()
  private val complexRequester by viewModels<ComplexRequester>()

  override fun getDataBindingConfig(): DataBindingConfig {
    return DataBindingConfig(R.layout.activity_main, BR.state, states)
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   *  ~
   *  Through the only exit 'dispatcher.output()' uniformly receives the message pushed back
   *  by the Single Source of Truth, and processes the UI logic according to the ID shunting.
   */
  override fun onOutput() {
    messenger.output(this) { messages ->
      if (messages is Messages.FinishActivity) finish()
    }

    complexRequester.output(this) { complexEvent ->
      when (complexEvent) {
        is ComplexIntent.ResultTest1 -> Log.d("complexEvent", "---1")
        is ComplexIntent.ResultTest2 -> Log.d("complexEvent", "---2")
        is ComplexIntent.ResultTest3 -> Log.d("complexEvent", "---3")
        is ComplexIntent.ResultTest4 -> Log.d("complexEvent", "---4 " + complexEvent.count)
      }
    }
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   *  ~
   *  Through the unique entry 'dispatcher Input' sends a message to the Single Source of Truth,
   *  which processes the business logic and distributes the results internally.
   */
  override fun onInput() {
    super.onInput()

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
    // ~
    // Here you can see through dispatcher Input sends multiple events continuously without being overwritten

    complexRequester.input(ComplexIntent.ResultTest1())
    complexRequester.input(ComplexIntent.ResultTest2())
    complexRequester.input(ComplexIntent.ResultTest2())
    complexRequester.input(ComplexIntent.ResultTest2())
    complexRequester.input(ComplexIntent.ResultTest2())
    complexRequester.input(ComplexIntent.ResultTest3())
    complexRequester.input(ComplexIntent.ResultTest3())
    complexRequester.input(ComplexIntent.ResultTest3())
  }

  class MainStates : StateHolder()
}
