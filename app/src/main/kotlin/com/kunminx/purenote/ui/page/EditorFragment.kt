package com.kunminx.purenote.ui.page

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.kunminx.architecture.ui.bind.ClickProxy
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.page.StateHolder
import com.kunminx.architecture.ui.state.State
import com.kunminx.architecture.utils.ToastUtils
import com.kunminx.architecture.utils.Utils
import com.kunminx.purenote.BR
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.event.NoteEvent
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.NoteRequester
import java.util.*

/**
 * Create by KunMinX at 2022/6/30
 */
class EditorFragment : BaseFragment() {
  private val states by viewModels<EditorStates>()
  private val noteRequester by viewModels<NoteRequester>()
  private val messenger by activityViewModels<PageMessenger>()
  private val clickProxy by lazy { ClickProxy() }

  override fun getDataBindingConfig(): DataBindingConfig {
    return DataBindingConfig(R.layout.fragment_editor, BR.state, states)
      .addBindingParam(BR.click, clickProxy)
  }

  override fun onInitData() {
    if (arguments != null) {
      states.tempNote.set(requireArguments().getParcelable(NOTE)!!)
      states.title.set(states.tempNote.get()?.title!!)
      states.content.set(states.tempNote.get()?.content!!)
      if (TextUtils.isEmpty(states.tempNote.get()?.id)) {
        states.titleRequestFocus.set(true)
      } else {
        states.tip.set(getString(R.string.last_time_modify))
        states.time.set(states.tempNote.get()?.modifyDate!!)
      }
    }
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   *  ~
   *  Through the only exit 'dispatcher.output()' uniformly receives the message pushed back
   *  by the Single Source of Truth, and processes the UI logic according to the ID shunting.
   */
  override fun onOutput() {
    noteRequester.output(this) { noteEvent ->
      if (noteEvent is NoteEvent.AddItem) {
        messenger.input(Messages.RefreshNoteList)
        ToastUtils.showShortToast(getString(R.string.saved))
        nav().navigateUp()
      }
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
    clickProxy.setOnClickListener { v -> if (v.id == R.id.btn_back) save() }
  }

  private fun save(): Boolean {
    val tempNote = states.tempNote.get()
    val title = states.title.get()
    val content = states.content.get()
    val empty = TextUtils.isEmpty(title + content)
    val unChanged = tempNote?.title == title && tempNote?.content == content
    if (empty || unChanged) return nav().navigateUp()

    val time = System.currentTimeMillis()
    val note: Note = if (tempNote?.id?.isEmpty()!!) {
      Note(UUID.randomUUID().toString(), title!!, content!!, time, time, 0)
    } else {
      Note(tempNote.id, title!!, content!!, tempNote.createTime, time, tempNote.type)
    }
    noteRequester.input(NoteEvent.AddItem(param = note))
    return true
  }

  override fun onBackPressed(): Boolean {
    return save()
  }

  /**
   * TODO tip 3：本项目为三层架构，即 表现层、领域层、数据层
   *  StateHolder 属于表现层，MVI-Dispatcher 属于领域层，
   *  领域层组件通过 PublishSubject（例如 SharedFlow）分发结果至表现层，
   *  对于状态，由 BehaviorSubject（例如以下 State 组件）响应和兜着；对于事件，则一次性执行，
   *
   * 具体可参见《解决 MVI 实战痛点》解析
   * https://juejin.cn/post/7134594010642907149
   */
  class EditorStates : StateHolder() {
    val tempNote = State<Note>(Note())
    val title = State("")
    val content = State("")
    val tip: State<String> = State(Utils.app?.getString(R.string.edit)!!)
    val time: State<String> = State(Utils.app?.getString(R.string.new_note)!!)
    val titleRequestFocus = State(false)
  }

  companion object {
    private const val NOTE = "NOTE"
    fun start(controller: NavController, note: Note?) {
      val bundle = Bundle()
      bundle.putParcelable(NOTE, note)
      controller.navigate(R.id.action_ListFragment_to_EditorFragment, bundle)
    }
  }
}
