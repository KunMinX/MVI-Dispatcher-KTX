package com.kunminx.purenote.domain.event

import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/16
 */
sealed class NoteEvent(var note: Note? = null) {
  data class GetNoteList(var notes: MutableList<Note>? = null) : NoteEvent()
  data class RemoveItem(var isSuccess: Boolean = true) : NoteEvent()
  data class UpdateItem(var isSuccess: Boolean = true) : NoteEvent()
  data class MarkItem(var isSuccess: Boolean = true) : NoteEvent()
  data class ToppingItem(var isSuccess: Boolean = true) : NoteEvent()
  data class AddItem(var isSuccess: Boolean = true) : NoteEvent()
}