package com.kunminx.purenote.domain.event

/**
 * Create by KunMinX at 2022/6/16
 */
sealed class Messages {
  data class RefreshNoteList(val b: Boolean = true) : Messages()
  data class FinishActivity(val b: Boolean = true) : Messages()
}