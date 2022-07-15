package com.kunminx.purenote.ui.page

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
  private lateinit var binding: FragmentEditorBinding
  private val states by viewModels<EditorViewModel>()
  private val noteRequester by viewModels<NoteRequester>()
  private val messenger by activityViewModels<PageMessenger>()

  override fun onInitView(inflater: LayoutInflater, container: ViewGroup?): View {
    binding = FragmentEditorBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onInitData() {
    if (arguments != null) {
      states.tempNote = requireArguments().getParcelable(NOTE)!!
      states.title = states.tempNote.title!!
      states.content = states.tempNote.content!!
      if (!TextUtils.isEmpty(states.tempNote.id)) {
        binding.etTitle.setText(states.tempNote.title)
        binding.etContent.setText(states.tempNote.content)
        binding.tvTitle.text = getString(R.string.last_time_modify)
        binding.tvTime.text = states.tempNote.modifyDate
      }
    }
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    noteRequester.output(this) { noteEvent ->
      if (noteEvent.eventId == NoteEvent.EVENT_ADD_ITEM) {
        messenger.input(Messages(Messages.EVENT_REFRESH_NOTE_LIST))
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
    binding.btnBack.setOnClickListener { save() }
  }

  private fun save() {
    states.tempNote.title = Objects.requireNonNull(binding.etTitle.text).toString()
    states.tempNote.content = Objects.requireNonNull(binding.etContent.text).toString()
    if (TextUtils.isEmpty(states.tempNote.title) && TextUtils.isEmpty(states.tempNote.content)
      || states.tempNote.title == states.title && states.tempNote.content == states.content
    ) {
      nav().navigateUp()
      return
    }
    val time = System.currentTimeMillis()
    if (TextUtils.isEmpty(states.tempNote.id)) {
      states.tempNote.createTime = time
      states.tempNote.id = UUID.randomUUID().toString()
    }
    states.tempNote.modifyTime = time
    noteRequester.input(NoteEvent(NoteEvent.EVENT_ADD_ITEM).setNote(states.tempNote))
  }

  override fun onBackPressed(): Boolean {
    save()
    return super.onBackPressed()
  }

  class EditorViewModel : ViewModel() {
    var tempNote: Note = Note()
    var title: String = ""
    var content: String = ""
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