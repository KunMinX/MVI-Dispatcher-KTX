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
import com.kunminx.purenote.databinding.FragmentListBinding
import com.kunminx.purenote.domain.event.Messages
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.ArrayList

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
    mMessenger.output(this, Observer { messages: Messages ->
      if (messages.eventId == Messages.Companion.EVENT_REFRESH_NOTE_LIST) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_GET_NOTE_LIST))
      }
    })
    mNoteRequester.output(this, Observer { noteEvent: NoteEvent ->
      when (noteEvent.eventId) {
        NoteEvent.Companion.EVENT_TOPPING_ITEM, NoteEvent.Companion.EVENT_GET_NOTE_LIST -> {
          mStates!!.list = noteEvent.result!!.notes
          mAdapter!!.setData(mStates!!.list)
          mBinding!!.ivEmpty.visibility =
            if (mStates!!.list!!.size == 0) View.VISIBLE else View.GONE
        }
        NoteEvent.Companion.EVENT_MARK_ITEM -> {}
        NoteEvent.Companion.EVENT_REMOVE_ITEM -> {}
      }
    })
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    mAdapter!!.setListener { viewId: Int, position: Int, item: Note? ->
      if (viewId == R.id.btn_mark) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_MARK_ITEM).setNote(item))
      } else if (viewId == R.id.btn_topping) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_TOPPING_ITEM).setNote(item))
      } else if (viewId == R.id.btn_delete) {
        mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_REMOVE_ITEM).setNote(item))
      } else if (viewId == R.id.cl) {
        EditorFragment.Companion.start(nav(), item)
      }
    }
    mBinding!!.fab.setOnClickListener { v: View? -> EditorFragment.Companion.start(nav(), Note()) }
    mNoteRequester!!.input(NoteEvent(NoteEvent.Companion.EVENT_GET_NOTE_LIST))
  }

  override fun onBackPressed(): Boolean {
    mMessenger!!.input(Messages(Messages.Companion.EVENT_FINISH_ACTIVITY))
    return super.onBackPressed()
  }

  class ListViewModel : ViewModel() {
    var list: List<Note?>? = ArrayList()
  }
}