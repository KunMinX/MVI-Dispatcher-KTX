package com.kunminx.purenote.ui.page

import android.text.TextUtils
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
import com.kunminx.purenote.domain.event.ApiEvent
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.event.NoteEvent
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.HttpRequester
import com.kunminx.purenote.domain.request.NoteRequester
import com.kunminx.purenote.ui.adapter.NoteAdapter

/**
 * Create by KunMinX at 2022/6/30
 */
class ListFragment : BaseFragment() {
  private val states by viewModels<ListStates>()
  private val noteRequester by viewModels<NoteRequester>()
  private val httpRequester by viewModels<HttpRequester>()
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
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   *  ~
   *  Through the only exit 'dispatcher.output()' uniformly receives the message pushed back
   *  by the Single Source of Truth, and processes the UI logic according to the ID shunting.
   */
  override fun onOutput() {
    messenger.output(this) { messages ->
      if (messages is Messages.RefreshNoteList) {
        noteRequester.input(NoteEvent.GetNoteList())
      }
    }
    noteRequester.output(this) { noteEvent ->
      when (noteEvent) {
        is NoteEvent.GetNoteList -> {
          adapter.refresh(noteEvent.notes!!)
          states.emptyViewShow.set(states.list.isEmpty())
        }
        is NoteEvent.ToppingItem -> {}
        is NoteEvent.MarkItem -> {}
        is NoteEvent.RemoveItem -> {}
        else -> {}
      }
    }

    httpRequester.output(this) { apiEvent ->
      when (apiEvent) {
        is ApiEvent.GetWeatherInfo -> states.weather.set(apiEvent.live?.weather!!)
        is ApiEvent.Error -> {}
      }
      states.loadingWeather.set(false)
    }
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   *  ~
   *  Through the unique entry 'dispatcher Input' sends a message to the Single Source of Truth,
   *  which processes the business logic and distributes the results internally.
   */
  override fun onInput() {
    adapter.setOnItemClick { viewId, item, position ->
      when (viewId) {
        R.id.btn_mark -> noteRequester.input(NoteEvent.MarkItem(param = item.copy()))
        R.id.btn_topping -> noteRequester.input(NoteEvent.ToppingItem(param = item.copy()))
        R.id.btn_delete -> noteRequester.input(NoteEvent.RemoveItem(param = item))
        R.id.cl -> EditorFragment.start(nav(), item)
      }
    }
    clickProxy.setOnClickListener { view ->
      if (view.id == R.id.fab) EditorFragment.start(nav(), Note())
    }
    if (TextUtils.isEmpty(states.weather.get())) {
      states.loadingWeather.set(true)
      httpRequester.input(ApiEvent.GetWeatherInfo().setCityCode(ApiEvent.CITY_CODE_BEIJING))
    }
    if (states.list.isEmpty()) noteRequester.input(NoteEvent.GetNoteList())
  }

  override fun onBackPressed(): Boolean {
    messenger.input(Messages.FinishActivity)
    return super.onBackPressed()
  }

  class ListStates : StateHolder() {
    val list = mutableListOf<Note>()
    val emptyViewShow = State(false)
    val loadingWeather = State(false)
    val weather = State("")
  }
}
