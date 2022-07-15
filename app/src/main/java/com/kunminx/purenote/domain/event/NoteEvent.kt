package com.kunminx.purenote.domain.event

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
import com.kunminx.architecture.domain.event.Event
import com.kunminx.purenote.data.bean.Note
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by KunMinX at 2022/6/16
 */
class NoteEvent(eventId: Int) : Event<NoteEvent.Param?, NoteEvent.Result?>() {
  fun setNote(note: Note?): NoteEvent {
    param!!.note = note
    return this
  }

  class Param {
    var note: Note? = null
  }

  class Result {
    var notes: List<Note>? = null
    var isSuccess = false
  }

  companion object {
    const val EVENT_GET_NOTE_LIST = 1
    const val EVENT_REMOVE_ITEM = 2
    const val EVENT_UPDATE_ITEM = 3
    const val EVENT_MARK_ITEM = 4
    const val EVENT_TOPPING_ITEM = 5
    const val EVENT_ADD_ITEM = 6
  }

  init {
    this.eventId = eventId
    param = Param()
    result = Result()
  }
}