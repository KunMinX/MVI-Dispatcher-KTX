package com.kunminx.purenote.ui.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.page.BaseFragment
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
class ListFragment : BaseFragment() {
  private lateinit var binding: FragmentListBinding
  private val states by viewModels<ListViewModel>()
  private val noteRequester by viewModels<NoteRequester>()
  private val messenger by activityViewModels<PageMessenger>()
  private val adapter by lazy { NoteAdapter() }

  override fun onInitView(inflater: LayoutInflater, container: ViewGroup?): View {
    binding = FragmentListBinding.inflate(layoutInflater, container, false)
    binding.rv.adapter = adapter
    return binding.root
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    messenger.output(this) { messages ->
      if (messages.eventId == Messages.EVENT_REFRESH_NOTE_LIST) {
        noteRequester.input(NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST))
      }
    }
    noteRequester.output(this) { noteEvent ->
      when (noteEvent.eventId) {
        NoteEvent.EVENT_TOPPING_ITEM, NoteEvent.EVENT_GET_NOTE_LIST -> {
          states.list = noteEvent.result?.notes!!
          adapter.setData(states.list)
          binding.ivEmpty.visibility = if (states.list.isEmpty()) View.VISIBLE else View.GONE
        }
        NoteEvent.EVENT_MARK_ITEM -> {}
        NoteEvent.EVENT_REMOVE_ITEM -> {}
      }
    }
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    adapter.setListener { viewId, position, item ->
      when (viewId) {
        R.id.btn_mark -> noteRequester.input(NoteEvent(NoteEvent.EVENT_MARK_ITEM).setNote(item))
        R.id.btn_topping -> noteRequester.input(NoteEvent(NoteEvent.EVENT_TOPPING_ITEM).setNote(item))
        R.id.btn_delete -> noteRequester.input(NoteEvent(NoteEvent.EVENT_REMOVE_ITEM).setNote(item))
        R.id.cl -> EditorFragment.start(nav(), item)
      }
    }
    binding.fab.setOnClickListener { EditorFragment.start(nav(), Note()) }
    noteRequester.input(NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST))
  }

  override fun onBackPressed(): Boolean {
    messenger.input(Messages(Messages.EVENT_FINISH_ACTIVITY))
    return super.onBackPressed()
  }

  class ListViewModel : ViewModel() {
    var list: MutableList<Note> = mutableListOf()
  }
}