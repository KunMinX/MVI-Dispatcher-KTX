package com.kunminx.purenote.domain.request

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
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by KunMinX at 2022/6/14
 */
class NoteRequester : MviDispatcher<NoteEvent?>() {
  /**
   * TODO tip 1：
   * 作为 '唯一可信源'，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   * ~
   * 与此同时，作为唯一可信源成熟态，
   * 自动消除 “mutable 样板代码 + LiveData 连发事件覆盖 + LiveData.setValue 误用滥用” 高频痛点。
   */
  override fun input(event: NoteEvent) {
    when (event.eventId) {
      NoteEvent.Companion.EVENT_GET_NOTE_LIST -> DataRepository.Companion.getInstance().getNotes(
        DataResult.Result { dataResult: DataResult<List<Note?>?> ->
          event.result!!.notes = dataResult.result
          sendResult(event)
        })
      NoteEvent.Companion.EVENT_UPDATE_ITEM, NoteEvent.Companion.EVENT_MARK_ITEM -> DataRepository.Companion.getInstance()
        .updateNote(
          event.param!!.note, DataResult.Result { dataResult: DataResult<Boolean> ->
            event.result!!.isSuccess = dataResult.result
            sendResult(event)
          })
      NoteEvent.Companion.EVENT_TOPPING_ITEM -> DataRepository.Companion.getInstance().updateNote(
        event.param!!.note, DataResult.Result { dataResult: DataResult<Boolean> ->
          event.result!!.isSuccess = dataResult.result
          if (event.result!!.isSuccess) {
            DataRepository.Companion.getInstance()
              .getNotes(DataResult.Result { dataResult1: DataResult<List<Note?>?> ->
                event.result!!.notes = dataResult1.result
                sendResult(event)
              })
          }
        })
      NoteEvent.Companion.EVENT_ADD_ITEM -> DataRepository.Companion.getInstance().insertNote(
        event.param!!.note, DataResult.Result { dataResult: DataResult<Boolean> ->
          event.result!!.isSuccess = dataResult.result
          sendResult(event)
        })
      NoteEvent.Companion.EVENT_REMOVE_ITEM -> DataRepository.Companion.getInstance().deleteNote(
        event.param!!.note, DataResult.Result { dataResult: DataResult<Boolean> ->
          event.result!!.isSuccess = dataResult.result
          sendResult(event)
        })
    }
  }
}