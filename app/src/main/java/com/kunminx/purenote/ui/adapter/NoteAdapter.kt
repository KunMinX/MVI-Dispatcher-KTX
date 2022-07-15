package com.kunminx.purenote.ui.adapter

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
import com.kunminx.architecture.ui.adapter.BaseAdapter
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.AdapterNoteListBinding
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by KunMinX at 2022/7/3
 */
class NoteAdapter : BaseAdapter<Note?, AdapterNoteListBinding?>() {
  protected override fun onBindingData(
    holder: BaseHolder<AdapterNoteListBinding>,
    note: Note,
    position: Int
  ) {
    holder.binding.tvTitle.text = note.title
    holder.binding.cl.clipToOutline = true
    holder.binding.btnMark.setImageResource(if (note.isMarked) R.drawable.icon_star else R.drawable.icon_star_board)
    holder.binding.tvTime.text = note.modifyDate
    holder.binding.tvTopped.visibility = if (note.isTopping) View.VISIBLE else View.GONE
    holder.binding.cl.setOnClickListener { v: View ->
      if (listener != null) listener.onItemClick(
        v.id,
        position,
        note
      )
    }
    holder.binding.btnMark.setOnClickListener { v: View ->
      note.toggleType(Note.Companion.TYPE_MARKED)
      notifyItemChanged(position)
      notifyItemRangeChanged(position, 1)
      if (listener != null) listener.onItemClick(v.id, position, note)
    }
    holder.binding.btnTopping.setOnClickListener { v: View ->
      note.toggleType(Note.Companion.TYPE_TOPPING)
      if (listener != null) listener.onItemClick(v.id, position, note)
    }
    holder.binding.btnDelete.setOnClickListener { v: View ->
      notifyItemRemoved(position)
      data.removeAt(position)
      notifyItemRangeRemoved(position, data.size - position)
      if (listener != null) listener.onItemClick(v.id, position, note)
    }
  }

  override fun onBindingView(viewGroup: ViewGroup): AdapterNoteListBinding {
    return AdapterNoteListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
  }
}