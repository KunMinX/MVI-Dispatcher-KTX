package com.kunminx.purenote.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kunminx.architecture.ui.adapter.BaseAdapter
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.AdapterNoteListBinding

/**
 * Create by KunMinX at 2022/7/3
 */
class NoteAdapter : BaseAdapter<Note, AdapterNoteListBinding>() {
  override fun onBindingData(
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
      if (onItemClick != null) onItemClick!!.invoke(v.id, position, note)
    }
    holder.binding.btnMark.setOnClickListener { v: View ->
      note.toggleType(Note.TYPE_MARKED)
      notifyItemChanged(position)
      notifyItemRangeChanged(position, 1)
      if (onItemClick != null) onItemClick!!.invoke(v.id, position, note)
    }
    holder.binding.btnTopping.setOnClickListener { v: View ->
      note.toggleType(Note.TYPE_TOPPING)
      if (onItemClick != null) onItemClick!!.invoke(v.id, position, note)
    }
    holder.binding.btnDelete.setOnClickListener { v: View ->
      notifyItemRemoved(position)
      getData().removeAt(position)
      notifyItemRangeRemoved(position, getData().size - position)
      if (onItemClick != null) onItemClick!!.invoke(v.id, position, note)
    }
  }

  override fun onBindingView(viewGroup: ViewGroup): AdapterNoteListBinding {
    return AdapterNoteListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
  }
}