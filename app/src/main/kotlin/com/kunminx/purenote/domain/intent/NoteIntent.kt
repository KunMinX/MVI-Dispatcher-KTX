package com.kunminx.purenote.domain.intent

import com.kunminx.purenote.data.bean.Note

/**
 * TODO tip：此 Intent 非传统意义上的 MVI intent，
 *  而是简化 reduce 和 action 后，拍平的 intent，
 *  它可以携带 param，input 至 mvi-Dispatcher，
 *  然后可以 copy 和携带 result，output 至表现层，
 *
 *  具体可参见《解决 MVI 实战痛点》解析
 *  https://juejin.cn/post/7134594010642907149
 *
 * Create by KunMinX at 2022/6/16
 */
sealed class NoteIntent {
  data class GetNoteList(
    val notes: List<Note>? = null
  ) : NoteIntent()

  data class RemoveItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteIntent()

  data class UpdateItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteIntent()

  data class MarkItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteIntent()

  data class ToppingItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteIntent()

  data class AddItem(
    val param: Note? = null,
    val isSuccess: Boolean = true
  ) : NoteIntent()

  data class InitItem(
    val param: Note? = null
  ) : NoteIntent()
}
