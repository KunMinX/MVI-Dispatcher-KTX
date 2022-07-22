package com.kunminx.purenote.ui.page

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.dylanc.viewbinding.binding
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
class EditorFragment : BaseFragment((R.layout.fragment_editor)) {
  private val binding: FragmentEditorBinding by binding()
  private val states by viewModels<EditorViewModel>()
  private val noteRequester by viewModels<NoteRequester>()
  private val messenger by activityViewModels<PageMessenger>()

  override fun onInitData() {
    if (arguments != null) {
      states.originNote = requireArguments().getParcelable(NOTE)!!
      states.title = states.originNote!!.title
      states.content = states.originNote!!.content
      if (TextUtils.isEmpty(states.originNote!!.id)) {
        binding.etTitle.requestFocus()
        binding.etTitle.post { toggleSoftInput() }
      } else {
        binding.etTitle.setText(states.originNote!!.title)
        binding.etContent.setText(states.originNote!!.content)
        binding.tvTitle.text = getString(R.string.last_time_modify)
        binding.tvTime.text = states.originNote!!.modifyDate
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
    binding.btnBack.setOnClickListener { save() }
  }

  private fun save(): Boolean {
    val title = binding.etTitle.text.toString()
    val content = binding.etContent.text.toString()
    if (title.isEmpty() && content.isEmpty() || title == states.title && content == states.content) {
      return nav().navigateUp()
    }
    val time = System.currentTimeMillis()
    if (states.originNote!!.id.isEmpty()) {
      states.tempNote = Note(UUID.randomUUID().toString(), title, content, time, time, 0)
    } else {
      states.tempNote = Note(
        states.originNote!!.id,
        title,
        content,
        states.originNote!!.createTime,
        time,
        states.originNote!!.type
      )
    }
    noteRequester.input(NoteEvent.AddItem().setNote(states.tempNote!!))
    return true
  }

  override fun onBackPressed(): Boolean {
    return save()
  }

  class EditorViewModel : ViewModel() {
    var originNote: Note? = Note()
    var tempNote: Note? = Note()
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
