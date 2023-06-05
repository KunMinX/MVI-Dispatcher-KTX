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
   *  通过唯一出口 'dispatcher.output' 统一接收 '可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
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
   *  通过唯一入口 'dispatcher.input' 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
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
    //    if (TextUtils.isEmpty(states.weather.get())) httpRequester.input(Api.GetWeatherInfo())

    if (states.list.isEmpty()) noteRequester.input(NoteIntent.GetNoteList())
  }

  override fun onBackPressed() {
    messenger.input(Messages.FinishActivity)
  }

  /**
   * TODO tip 3：传统 MVI 属于对响应式编程的填坑和升级，
   *  故通常是两层架构：表现层和数据层，
   *  ViewModel 是表现层组件，业务逻辑状态改变都在 ViewModel 中写，
   *  响应式编程便于单元测试和关注点分离，同时也使同质化的业务逻辑分散在多个 ViewModel 中，易造成修改时的不一致，
   *  ~
   *  故综合考虑，本项目示例采用三层架构，即 表现层、领域层、数据层，
   *  StateHolder 属于表现层，为页面专属，MVI-Dispatcher 属于领域层，可供同业务不同页面复用，
   *  领域层组件通过 PublishSubject（例如 SharedFlow）分发结果至表现层，
   *  对于状态，由 BehaviorSubject（例如以下 State 组件）响应和兜着，对于事件，则一次性执行，
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
