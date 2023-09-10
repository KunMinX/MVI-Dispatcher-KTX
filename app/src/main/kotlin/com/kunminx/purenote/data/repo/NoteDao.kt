package com.kunminx.purenote.data.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kunminx.purenote.data.bean.Note
import kotlinx.coroutines.flow.Flow

/**
 * Create by KunMinX at 2022/6/14
 */
@Dao
interface NoteDao {
  @Query("select * from note order by type & 0x0001 = 0x0001 desc, modify_time desc")
  fun notes(): List<Note>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertNote(note: Note)

  @Update
  fun updateNote(note: Note)

  @Delete
  fun deleteNote(note: Note)
}
