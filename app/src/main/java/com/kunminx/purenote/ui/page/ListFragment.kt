package com.kunminx.purenote.ui.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
  private var mBinding: FragmentListBinding? = null
  private var mStates: ListViewModel? = null
  private var mNoteRequester: NoteRequester? = null
  private var mMessenger: PageMessenger? = null
  private var mAdapter: NoteAdapter? = null
  override fun onInitViewModel() {
    mStates = getFragmentScopeViewModel(ListViewModel::class.java)
    mNoteRequester = getFragmentScopeViewModel(NoteRequester::class.java)
    mMessenger = getApplicationScopeViewModel(PageMessenger::class.java)
  }

  override fun onInitView(inflater: LayoutInflater, container: ViewGroup?): View {
    mBinding = FragmentListBinding.inflate(inflater, container, false)
    mBinding!!.rv.adapter = NoteAdapter().also { mAdapter = it }
    return mBinding!!.root
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    mMessenger?.output(this) { messages ->
      if (messages.eventId == Messages.EVENT_REFRESH_NOTE_LIST) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST))
      }
    }
    mNoteRequester?.output(this) { noteEvent ->
      when (noteEvent.eventId) {
        NoteEvent.EVENT_TOPPING_ITEM, NoteEvent.EVENT_GET_NOTE_LIST -> {
          mStates!!.list = noteEvent.result!!.notes
          mAdapter!!.setData(mStates!!.list)
          mBinding!!.ivEmpty.visibility =
            if (mStates!!.list!!.isEmpty()) View.VISIBLE else View.GONE
        }
        NoteEvent.EVENT_MARK_ITEM -> {}
        NoteEvent.EVENT_REMOVE_ITEM -> {}
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    mAdapter!!.setListener { viewId, position, item ->
      if (viewId == R.id.btn_mark) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_MARK_ITEM).setNote(item))
      } else if (viewId == R.id.btn_topping) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_TOPPING_ITEM).setNote(item))
      } else if (viewId == R.id.btn_delete) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_REMOVE_ITEM).setNote(item))
      } else if (viewId == R.id.cl) {
        EditorFragment.start(nav(), item)
      }
    }
    mBinding!!.fab.setOnClickListener { EditorFragment.start(nav(), Note()) }
    mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_GET_NOTE_LIST))
  }

  override fun onBackPressed(): Boolean {
    mMessenger!!.input(Messages(Messages.EVENT_FINISH_ACTIVITY))
    return super.onBackPressed()
  }

  class ListViewModel : ViewModel() {
    var list: List<Note?>? = ArrayList()
  }
}