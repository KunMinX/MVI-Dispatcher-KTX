package com.kunminx.purenote.data.repo

import androidx.room.Room
import com.kunminx.architecture.utils.Utils
import com.kunminx.purenote.data.bean.Note
import kotlinx.coroutines.flow.Flow

/**
 * Create by KunMinX at 2022/6/14
 */
class DataRepository private constructor() {
  private val dataBase: NoteDataBase = Room.databaseBuilder(
    Utils.app!!.applicationContext,
    NoteDataBase::class.java, DATABASE_NAME
  ).build()

  fun getNotes(): Flow<List<Note>> {
    return dataBase.noteDao().notes()
  }

  suspend fun insertNote(note: Note): Boolean {
    dataBase.noteDao().insertNote(note)
    return true
  }

  suspend fun updateNote(note: Note): Boolean {
    dataBase.noteDao().updateNote(note)
    return true
  }

  suspend fun deleteNote(note: Note): Boolean {
    dataBase.noteDao().deleteNote(note)
    return true
  }

  companion object {
    val instance = DataRepository()
    private const val DATABASE_NAME = "NOTE_DB.db"
  }
}