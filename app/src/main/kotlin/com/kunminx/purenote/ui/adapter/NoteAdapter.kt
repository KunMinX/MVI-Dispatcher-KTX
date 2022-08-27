package com.kunminx.purenote.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.kunminx.architecture.ui.adapter.BaseBindingAdapter
import com.kunminx.purenote.R
import com.kunminx.purenote.data.bean.Note
import com.kunminx.purenote.databinding.AdapterNoteListBinding

/**
 * Create by KunMinX at 2022/7/3
 */
class NoteAdapter(list: MutableList<Note>) :
  BaseBindingAdapter<Note, AdapterNoteListBinding>(list) {
  override fun getLayoutResId(viewType: Int): Int {
    return R.layout.adapter_note_list
  }

  override fun onBindItem(
    binding: AdapterNoteListBinding,
    note: Note,
    holder: RecyclerView.ViewHolder
  ) {
    binding.note = note
    val position = holder.bindingAdapterPosition
    binding.cl.setOnClickListener { v ->
      itemClick?.invoke(v.id, note, position)
    }
    binding.btnMark.setOnClickListener { v ->
      note.toggleType(Note.TYPE_MARKED)
      notifyItemChanged(position)
      notifyItemRangeChanged(position, 1)
      itemClick?.invoke(v.id, note, position)
    }
    binding.btnTopping.setOnClickListener { v ->
      note.toggleType(Note.TYPE_TOPPING)
      itemClick?.invoke(v.id, note, position)
    }
    binding.btnDelete.setOnClickListener { v ->
      notifyItemRemoved(position)
      _list.removeAt(position)
      notifyItemRangeRemoved(position, list.size - position)
      itemClick?.invoke(v.id, note, position)
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }
}
