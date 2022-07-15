package com.kunminx.purenote.data.repo

import androidx.room.Room
import com.kunminx.architecture.data.response.AsyncTask
import com.kunminx.architecture.data.response.DataResult
import com.kunminx.architecture.utils.Utils
import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/14
 */
class DataRepository private constructor() {
  private val dataBase: NoteDataBase = Room.databaseBuilder(
    Utils.app!!.applicationContext,
    NoteDataBase::class.java, DATABASE_NAME
  ).build()

  fun getNotes(result: (dataResult: DataResult<MutableList<Note>>) -> Unit) {
    AsyncTask.doAction({
      dataBase.noteDao().notes
    }) { notes -> result.invoke(DataResult(notes)) }
  }

  fun insertNote(note: Note, result: (dataResult: DataResult<Boolean>) -> Unit) {
    AsyncTask.doAction({
      dataBase.noteDao().insertNote(note)
      true
    }) { success -> result.invoke(DataResult(success)) }
  }

  fun updateNote(note: Note, result: (dataResult: DataResult<Boolean>) -> Unit) {
    AsyncTask.doAction({
      dataBase.noteDao().updateNote(note)
      true
    }) { success -> result.invoke(DataResult(success)) }
  }

  fun deleteNote(note: Note, result: (dataResult: DataResult<Boolean>) -> Unit) {
    AsyncTask.doAction({
      dataBase.noteDao().deleteNote(note)
      true
    }) { success -> result.invoke(DataResult(success)) }
  }

  companion object {
    val instance = DataRepository()
    private const val DATABASE_NAME = "NOTE_DB.db"
  }

}