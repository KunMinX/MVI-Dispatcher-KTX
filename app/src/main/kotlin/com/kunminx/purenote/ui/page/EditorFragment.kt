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
import com.kunminx.purenote.domain.intent.Messages
import com.kunminx.purenote.domain.intent.NoteIntent
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

  /**
   * TODO tip 1：
   * 在 PublishSubject 的唯一出口 output{ ... } 中响应数据的变化，
   * 对于一次性事件，直接执行，
   * 对于状态数据，通过 BehaviorSubject 通知所绑定控件属性重新渲染，并为其兜住最后一次状态，
   */
  override fun onOutput() {
    noteRequester.output(this) {
      if (it is NoteIntent.InitItem) {
        states.tempNote.set(it.param?.copy()!!)
        states.title.set(states.tempNote.get()?.title!!)
        states.content.set(states.tempNote.get()?.content!!)
        if (TextUtils.isEmpty(states.tempNote.get()?.id)) {
          states.titleRequestFocus.set(true)
        } else {
          states.tip.set(getString(R.string.last_time_modify))
          states.time.set(states.tempNote.get()?.modifyDate!!)
        }
      } else if (it is NoteIntent.AddItem) {
        messenger.input(Messages.RefreshNoteList)
        ToastUtils.showShortToast(getString(R.string.saved))
        nav().navigateUp()
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 input() 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    clickProxy.setOnClickListener { v -> if (v.id == R.id.btn_back) save() }
    noteRequester.input(NoteIntent.InitItem(requireArguments().getParcelable(NOTE)!!))
  }

  private fun save() {
    val tempNote = states.tempNote.get()
    val title = states.title.get()
    val content = states.content.get()
    val empty = TextUtils.isEmpty(title + content)
    val unChanged = tempNote?.title == title && tempNote?.content == content
    if (empty || unChanged) nav().navigateUp()

    val time = System.currentTimeMillis()
    val note: Note = if (tempNote?.id?.isEmpty()!!) {
      Note(UUID.randomUUID().toString(), title!!, content!!, time, time, 0)
    } else {
      Note(tempNote.id, title!!, content!!, tempNote.createTime, time, tempNote.type)
    }
    noteRequester.input(NoteIntent.AddItem(param = note))
  }

  override fun onBackPressed() {
    save()
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
  class EditorStates : StateHolder() {
    val tempNote = State(Note())
    val title = State("")
    val content = State("")
    val tip = State(Utils.app?.getString(R.string.edit)!!)
    val time = State(Utils.app?.getString(R.string.new_note)!!)
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
