package com.kunminx.purenote.domain.event

import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/16
 */
sealed class NoteEvent {
  data class GetNoteList(
    val notes: List<Note>? = null
  ) : NoteEvent()

  data class RemoveItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteEvent()

  data class UpdateItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteEvent()

  data class MarkItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteEvent()

  data class ToppingItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteEvent()

  data class AddItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteEvent()
}
