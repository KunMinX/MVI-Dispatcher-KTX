package com.kunminx.purenote.ui.page

import androidx.navigation.NavController.navigate
import androidx.navigation.NavController.navigateUp
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.purenote.ui.page.ListFragment.ListViewModel
import com.kunminx.purenote.domain.request.NoteRequester
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.ui.adapter.NoteAdapter
import com.kunminx.purenote.domain.event.NoteEvent
import com.kunminx.purenote.R
import com.kunminx.purenote.ui.page.EditorFragment
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.purenote.domain.request.ComplexRequester
import com.kunminx.purenote.domain.event.ComplexEvent
import com.kunminx.purenote.ui.page.EditorFragment.EditorViewModel
import android.text.TextUtils
import com.kunminx.architecture.utils.ToastUtils
import android.text.Editable
import androidx.navigation.NavController
import android.os.Bundle
import kotlin.jvm.JvmOverloads
import android.graphics.PointF
import com.kunminx.purenote.ui.view.SwipeMenuLayout
import android.content.res.TypedArray
import android.view.View.MeasureSpec
import android.view.ViewGroup.MarginLayoutParams
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.animation.OvershootInterpolator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateInterpolator
import com.kunminx.architecture.ui.adapter.BaseAdapter.BaseHolder
import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.os.Parcel
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kunminx.purenote.data.repo.NoteDao
import com.kunminx.purenote.data.repo.NoteDataBase
import com.kunminx.architecture.data.response.DataResult
import com.kunminx.architecture.data.response.AsyncTask.ActionStart
import com.kunminx.architecture.data.response.AsyncTask.ActionEnd
import com.kunminx.purenote.data.repo.DataRepository
import androidx.room.Room
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.FragmentEditorBinding
import com.kunminx.purenote.domain.event.Messages
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
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
      mStates!!.tempNote = arguments!!.getParcelable(NOTE)
      mStates!!.title = mStates!!.tempNote!!.title
      mStates!!.content = mStates!!.tempNote!!.content
      if (!TextUtils.isEmpty(mStates!!.tempNote!!.id)) {
        mBinding!!.etTitle.setText(mStates!!.tempNote!!.title)
        mBinding!!.etContent.setText(mStates!!.tempNote!!.content)
        mBinding!!.tvTitle.text = getString(R.string.last_time_modify)
        mBinding!!.tvTime.text = mStates!!.tempNote.getModifyDate()
      }
    }
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    mNoteRequester.output(this, Observer { noteEvent: NoteEvent ->
      if (noteEvent.eventId == NoteEvent.Companion.EVENT_ADD_ITEM) {
        mMessenger!!.input(Messages(Messages.Companion.EVENT_REFRESH_NOTE_LIST))
        ToastUtils.showShortToast(getString(R.string.saved))
        nav().navigateUp()
      }
    })
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    mBinding!!.btnBack.setOnClickListener { v: View? -> save() }
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
    mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_ADD_ITEM).setNote(mStates!!.tempNote))
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