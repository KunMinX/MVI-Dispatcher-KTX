package com.kunminx.purenote.data.repo

import androidx.room.*
import com.kunminx.purenote.data.bean.Note

/**
 * Create by KunMinX at 2022/6/14
 */
@Dao
interface NoteDao {
  @Query("select * from note order by type & 0x0001 = 0x0001 desc, modify_time desc")
  suspend fun notes(): MutableList<Note>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: Note)

  @Update
  suspend fun updateNote(note: Note)

  @Delete
  suspend fun deleteNote(note: Note)
}