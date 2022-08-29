package com.kunminx.purenote.domain.event

import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/16
 */
sealed class NoteEvent {
  var note: Note? = null
  fun setNote(note: Note): NoteEvent {
    this.note = note
    return this
  }

  data class GetNoteList(val notes: List<Note>? = null) : NoteEvent()
  data class RemoveItem(val isSuccess: Boolean = true) : NoteEvent()
  data class UpdateItem(val isSuccess: Boolean = true) : NoteEvent()
  data class MarkItem(val isSuccess: Boolean = true) : NoteEvent()
  data class ToppingItem(val isSuccess: Boolean = true) : NoteEvent()
  data class AddItem(val isSuccess: Boolean = true) : NoteEvent()
}
