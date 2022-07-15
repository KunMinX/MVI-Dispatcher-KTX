package com.kunminx.purenote.data.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/14
 */
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {
  abstract fun noteDao(): NoteDao
}