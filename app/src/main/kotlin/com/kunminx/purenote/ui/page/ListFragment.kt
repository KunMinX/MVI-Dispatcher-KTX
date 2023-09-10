package com.kunminx.purenote.ui.page

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kunminx.architecture.ui.bind.ClickProxy
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.page.StateHolder
import com.kunminx.architecture.ui.state.State
import com.kunminx.purenote.BR
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.domain.intent.Api
import com.kunminx.purenote.domain.intent.Messages
import com.kunminx.purenote.domain.intent.NoteIntent
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.NoteRequester
import com.kunminx.purenote.domain.request.WeatherRequester
import com.kunminx.purenote.ui.adapter.NoteAdapter

/**
 * Create by KunMinX at 2022/6/30
 */
class ListFragment : BaseFragment() {
  private val states by viewModels<ListStates>()
  private val noteRequester by viewModels<NoteRequester>()
  private val weatherRequester by viewModels<WeatherRequester>()
  private val messenger by activityViewModels<PageMessenger>()
  private val adapter by lazy { NoteAdapter(states.list) }
  private val clickProxy by lazy { ClickProxy() }

  override fun getDataBindingConfig(): DataBindingConfig {
    return DataBindingConfig(R.layout.fragment_list, BR.state, states)
      .addBindingParam(BR.adapter, adapter)
      .addBindingParam(BR.click, clickProxy)
  }

  /**
   * TODO tip 1：
   * 在 PublishSubject 的唯一出口 output{ ... } 中响应数据的变化，
   * 对于一次性事件，直接执行，
   * 对于状态数据，通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
   */
  override fun onOutput() {
    messenger.output(this) {
      if (it is Messages.RefreshNoteList) noteRequester.input(NoteIntent.GetNoteList())
    }
    noteRequester.output(this) {
      when (it) {
        is NoteIntent.GetNoteList -> {
          adapter.refresh(it.notes!!)
          states.emptyViewShow.set(states.list.isEmpty())
        }
        is NoteIntent.ToppingItem -> {}
        is NoteIntent.MarkItem -> {}
        is NoteIntent.RemoveItem -> {}
        else -> {}
      }
    }
    weatherRequester.output(this) {
      when (it) {
        is Api.Loading -> states.loadingWeather.set(it.isLoading)
        is Api.GetWeatherInfo -> states.weather.set(it.live?.weather!!)
        is Api.Error -> {}
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    adapter.setOnItemClick { viewId, item, position ->
      when (viewId) {
        R.id.btn_mark -> noteRequester.input(NoteIntent.MarkItem(param = item.copy()))
        R.id.btn_topping -> noteRequester.input(NoteIntent.ToppingItem(param = item.copy()))
        R.id.btn_delete -> noteRequester.input(NoteIntent.RemoveItem(param = item))
        R.id.cl -> EditorFragment.start(nav(), item)
      }
    }
    clickProxy.setOnClickListener { view ->
      if (view.id == R.id.fab) EditorFragment.start(nav(), Note())
    }

    //TODO 天气示例使用高德 API_KEY，如有需要，请自行在 "高德开放平台" 获取和在 Api 类填入
    //if (TextUtils.isEmpty(states.weather.get())) httpRequester.input(Api.GetWeatherInfo())

    if (states.list.isEmpty()) noteRequester.input(NoteIntent.GetNoteList())
  }

  override fun onBackPressed() {
    messenger.input(Messages.FinishActivity)
  }

  /**
   * TODO tip 3：
   * 基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
   * 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
   * 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  class ListStates : StateHolder() {
    val list = mutableListOf<Note>()
    val emptyViewShow = State(false)
    val loadingWeather = State(false)
    val weather = State("")
  }
}
