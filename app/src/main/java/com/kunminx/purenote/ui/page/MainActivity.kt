package com.kunminx.purenote.ui.page

import androidx.navigation.NavController.navigate
import androidx.navigation.NavController.navigateUp
import com.kunminx.architecture.ui.page.BaseFragment
import com.kunminx.purenote.ui.page.ListFragment.ListViewModel
import com.kunminx.purenote.domain.request.NoteRequester
import com.kunminx.purenote.domain.message.PageMessenger
import com.kunminx.purenote.ui.adapter.NoteAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
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
import android.view.VelocityTracker
import com.kunminx.purenote.ui.view.SwipeMenuLayout
import android.view.ViewConfiguration
import android.content.res.TypedArray
import android.view.View.MeasureSpec
import android.view.ViewGroup.MarginLayoutParams
import android.view.MotionEvent
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
import android.util.Log
import androidx.lifecycle.Observer
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
import com.kunminx.purenote.domain.event.Messages
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivity : BaseActivity() {
  private var mMessenger: PageMessenger? = null
  private var mComplexRequester: ComplexRequester? = null
  override fun onInitViewModel() {
    mMessenger = getApplicationScopeViewModel(PageMessenger::class.java)
    mComplexRequester = getActivityScopeViewModel(ComplexRequester::class.java)
  }

  override fun onInitView() {
    setContentView(R.layout.activity_main)
  }

  /**
   * TODO tip 1：
   * 通过唯一出口 'dispatcher.output' 统一接收 '唯一可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   */
  override fun onOutput() {
    mMessenger.output(
      this,
      Observer { messages: Messages -> if (messages.eventId == Messages.Companion.EVENT_FINISH_ACTIVITY) finish() })
    mComplexRequester.output(
      this,
      Observer { complexEvent: ComplexEvent ->
        if (complexEvent.eventId == ComplexEvent.Companion.EVENT_TEST_1) Log.d(
          "complexEvent",
          "---1"
        ) else if (complexEvent.eventId == ComplexEvent.Companion.EVENT_TEST_2) Log.d(
          "complexEvent",
          "---2"
        ) else if (complexEvent.eventId == ComplexEvent.Companion.EVENT_TEST_3) Log.d(
          "complexEvent",
          "---3"
        ) else if (complexEvent.eventId == ComplexEvent.Companion.EVENT_TEST_4) Log.d(
          "complexEvent",
          "---4 " + complexEvent.result!!.count
        )
      })
  }

  /**
   * TODO tip 2：
   * 通过唯一入口 'dispatcher.input' 发消息至 "唯一可信源"，由其内部统一处理业务逻辑和结果分发。
   */
  override fun onInput() {
    super.onInput()

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_1))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_2))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_3))
    mComplexRequester!!.input(ComplexEvent(ComplexEvent.Companion.EVENT_TEST_3))
  }
}