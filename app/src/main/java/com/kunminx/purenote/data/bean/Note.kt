package com.kunminx.purenote.data.bean

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
import android.os.Parcel
import androidx.room.*
import com.kunminx.purenote.data.repo.NoteDao
import com.kunminx.purenote.data.repo.NoteDataBase
import com.kunminx.architecture.data.response.DataResult
import com.kunminx.architecture.data.response.AsyncTask.ActionStart
import com.kunminx.architecture.data.response.AsyncTask.ActionEnd
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.architecture.domain.dispatch.MviDispatcher
import com.kunminx.architecture.utils.TimeUtils
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by KunMinX on 2015/7/31.
 */
@Entity
class Note : Parcelable {
  @JvmField
  @PrimaryKey
  var id = ""
  @JvmField
  var title: String? = ""
  @JvmField
  var content: String? = ""

  @JvmField
  @ColumnInfo(name = "create_time")
  var createTime: Long = 0

  @JvmField
  @ColumnInfo(name = "modify_time")
  var modifyTime: Long = 0
  @JvmField
  var type = 0

  @get:Ignore
  val createDate: String
    get() = TimeUtils.getTime(createTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val modifyDate: String
    get() = TimeUtils.getTime(modifyTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val isMarked: Boolean
    get() = type and TYPE_MARKED != 0

  @get:Ignore
  val isTopping: Boolean
    get() = type and TYPE_TOPPING != 0

  fun toggleType(param: Int) {
    type = if (type and param != 0) {
      type and param.inv()
    } else {
      type or param
    }
  }

  constructor() {}
  protected constructor(`in`: Parcel) {
    id = `in`.readString()!!
    title = `in`.readString()
    content = `in`.readString()
    createTime = `in`.readLong()
    modifyTime = `in`.readLong()
    type = `in`.readInt()
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(id)
    dest.writeString(title)
    dest.writeString(content)
    dest.writeLong(createTime)
    dest.writeLong(modifyTime)
    dest.writeInt(type)
  }

  companion object {
    const val TYPE_TOPPING = 0x0001
    const val TYPE_MARKED = 0x0002
    val CREATOR: Parcelable.Creator<Note> = object : Parcelable.Creator<Note?> {
      override fun createFromParcel(`in`: Parcel): Note? {
        return Note(`in`)
      }

      override fun newArray(size: Int): Array<Note?> {
        return arrayOfNulls(size)
      }
    }
  }
}