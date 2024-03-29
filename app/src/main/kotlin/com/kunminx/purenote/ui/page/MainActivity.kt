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
   * 在 PublishSubject 的唯一出口 output{ ... } 中响应数据的变化，
   * 对于一次性事件，直接执行，
   * 对于状态数据，通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
   */
  override fun onOutput() {
    messenger.output(this) {
      if (it is Messages.FinishActivity) finish()
    }

    complexRequester.output(this) {
      when (it) {
        is ComplexIntent.ResultTest1 -> Log.i("complexEvent", "---1")
        is ComplexIntent.ResultTest2 -> Log.i("complexEvent", "---2")
        is ComplexIntent.ResultTest3 -> Log.i("complexEvent", "---3")
        is ComplexIntent.ResultTest4 -> Log.i("complexEvent", "---4 " + it.count)
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    super.onInput()

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖

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
