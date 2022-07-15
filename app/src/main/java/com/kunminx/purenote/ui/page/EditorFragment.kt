package com.kunminx.purenote.ui.page

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.architecture.utils.ToastUtils
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.FragmentEditorBinding
import com.kunminx.purenote.domain.event.Messages
import com.kunminx.purenote.domain.event.NoteEvent
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.domain.request.NoteRequester
import java.util.*

/**
 * Create by KunMinX at 2022/6/30
 */
class EditorFragment : BaseFragment() {
  private var mBinding: FragmentEditorBinding? = null
  private var mStates: EditorViewModel? = null
  private var mNoteRequester: NoteRequester? = null
  private var mMessenger: PageMessenger? = null
  override fun onInitViewModel() {
    mStates = getFragmentScopeViewModel(EditorViewModel::class.java)
    mNoteRequester = getFragmentScopeViewModel(NoteRequester::class.java)
    mMessenger = getApplicationScopeViewModel(PageMessenger::class.java)
  }

  override fun onInitView(inflater: LayoutInflater, container: ViewGroup?): View {
    mBinding = FragmentEditorBinding.inflate(inflater, container, false)
    return mBinding!!.root
  }

  override fun onInitData() {
    if (arguments != null) {
      mStates!!.tempNote = requireArguments().getParcelable(NOTE)
      mStates!!.title = mStates!!.tempNote!!.title
      mStates!!.content = mStates!!.tempNote!!.content
      if (!TextUtils.isEmpty(mStates!!.tempNote!!.id)) {
        mBinding!!.etTitle.setText(mStates!!.tempNote!!.title)
        mBinding!!.etContent.setText(mStates!!.tempNote!!.content)
        mBinding!!.tvTitle.text = getString(R.string.last_time_modify)
        mBinding!!.tvTime.text = mStates!!.tempNote?.modifyDate
      }
    }
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    mNoteRequester?.output(this) { noteEvent ->
      if (noteEvent.eventId == NoteEvent.EVENT_ADD_ITEM) {
        mMessenger!!.input(Messages(Messages.EVENT_REFRESH_NOTE_LIST))
        ToastUtils.showShortToast(getString(R.string.saved))
        nav().navigateUp()
      }
    }
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    mBinding!!.btnBack.setOnClickListener { save() }
  }

  private fun save() {
    mStates!!.tempNote!!.title = Objects.requireNonNull(mBinding!!.etTitle.text).toString()
    mStates!!.tempNote!!.content = Objects.requireNonNull(mBinding!!.etContent.text).toString()
    if (TextUtils.isEmpty(mStates!!.tempNote!!.title) && TextUtils.isEmpty(mStates!!.tempNote!!.content)
      || mStates!!.tempNote!!.title == mStates!!.title && mStates!!.tempNote!!.content == mStates!!.content
    ) {
      nav().navigateUp()
      return
    }
    val time = System.currentTimeMillis()
    if (TextUtils.isEmpty(mStates!!.tempNote!!.id)) {
      mStates!!.tempNote!!.createTime = time
      mStates!!.tempNote!!.id = UUID.randomUUID().toString()
    }
    mStates!!.tempNote!!.modifyTime = time
    mNoteRequester!!.input(NoteEvent(NoteEvent.EVENT_ADD_ITEM).setNote(mStates!!.tempNote))
  }

  override fun onBackPressed(): Boolean {
    save()
    return super.onBackPressed()
  }

  class EditorViewModel : ViewModel() {
    var tempNote: Note? = Note()
    var title: String? = ""
    var content: String? = ""
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