package com.kunminx.purenote.ui.page

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dylanc.viewbinding.binding
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.architecture.ui.page.StateHolder
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.FragmentListBinding
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.event.NoteEvent
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.NoteRequester
import com.kunminx.purenote.ui.adapter.NoteAdapter

/**
 * Create by KunMinX at 2022/6/30
 */
class ListFragment : BaseFragment((R.layout.fragment_list)) {
  private val binding: FragmentListBinding by binding()
  private val states by viewModels<ListViewModel>()
  private val noteRequester by viewModels<NoteRequester>()
  private val messenger by activityViewModels<PageMessenger>()
  private val adapter by lazy { NoteAdapter() }

  override fun onInitView() {
    binding.rv.adapter = adapter
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
          states.list = noteEvent.notes!!.toMutableList()
          adapter.setData(states.list)
          binding.ivEmpty.visibility = if (states.list.isEmpty()) View.VISIBLE else View.GONE
        }
        is NoteEvent.ToppingItem -> {}
        is NoteEvent.MarkItem -> {}
        is NoteEvent.RemoveItem -> {}
        else -> {}
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
    adapter.setListener { viewId, position, item ->
      when (viewId) {
        R.id.btn_mark -> noteRequester.input(NoteEvent.MarkItem().setNote(item.copy()))
        R.id.btn_topping -> noteRequester.input(NoteEvent.ToppingItem().setNote(item.copy()))
        R.id.btn_delete -> noteRequester.input(NoteEvent.RemoveItem().setNote(item))
        R.id.cl -> EditorFragment.start(nav(), item)
      }
    }
    binding.fab.setOnClickListener { EditorFragment.start(nav(), Note()) }
    noteRequester.input(NoteEvent.GetNoteList())
  }

  override fun onBackPressed(): Boolean {
    messenger.input(Messages.FinishActivity)
    return super.onBackPressed()
  }

  class ListViewModel : StateHolder() {
    var list: MutableList<Note> = mutableListOf()
  }
}
