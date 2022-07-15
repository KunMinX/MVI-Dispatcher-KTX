package com.kunminx.purenote.data.repo

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
import com.kunminx.architecture.data.response.AsyncTask
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import com.kunminx.architecture.utils.Utils
import com.kunminx.purenote.data.bean.Note
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by KunMinX at 2022/6/14
 */
class DataRepository private constructor() {
  private val mDataBase: NoteDataBase
  fun getNotes(result: DataResult.Result<List<Note?>?>) {
    AsyncTask.doAction(
      { mDataBase.noteDao().notes }
    ) { notes: List<Note?>? -> result.onResult(DataResult(notes)) }
  }

  fun insertNote(note: Note?, result: DataResult.Result<Boolean>) {
    AsyncTask.doAction({
      mDataBase.noteDao().insertNote(note)
      true
    }) { success: Boolean -> result.onResult(DataResult(success)) }
  }

  fun updateNote(note: Note?, result: DataResult.Result<Boolean>) {
    AsyncTask.doAction({
      mDataBase.noteDao().updateNote(note)
      true
    }) { success: Boolean -> result.onResult(DataResult(success)) }
  }

  fun deleteNote(note: Note?, result: DataResult.Result<Boolean>) {
    AsyncTask.doAction({
      mDataBase.noteDao().deleteNote(note)
      true
    }) { success: Boolean -> result.onResult(DataResult(success)) }
  }

  companion object {
    val instance = DataRepository()
    private const val DATABASE_NAME = "NOTE_DB.db"
  }

  init {
    mDataBase = Room.databaseBuilder(
      Utils.app.applicationContext,
      NoteDataBase::class.java, DATABASE_NAME
    ).build()
  }
}