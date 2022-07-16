package com.kunminx.purenote.data.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kunminx.architecture.utils.TimeUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by KunMinX on 2015/7/31.
 */
@Entity
@Parcelize
data class Note(
  @PrimaryKey
  val id: String,
  val title: String,
  val content: String,
  @ColumnInfo(name = "create_time")
  val createTime: Long,
  @ColumnInfo(name = "modify_time")
  val modifyTime: Long,
  val type: Int
) : Parcelable {

  @Ignore
  constructor() : this("", "", "", 0, 0, 0)

  @get:Ignore
  val createDate: String
    get() = TimeUtils.getTime(createTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val modifyDate: String
    get() = TimeUtils.getTime(modifyTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val isMarked: Boolean
    get() = mutableType and TYPE_MARKED != 0

  @get:Ignore
  val isTopping: Boolean
    get() = mutableType and TYPE_TOPPING != 0

  @IgnoredOnParcel
  @Ignore
  var mutableType: Int = type

  @Ignore
  fun toggleType(param: Int) {
    mutableType = if (mutableType and param != 0) {
      mutableType and param.inv()
    } else {
      mutableType or param
    }
  }

  fun copy(): Note {
    return Note(id, title, content, createTime, modifyTime, mutableType)
  }

  companion object {
    const val TYPE_TOPPING = 0x0001
    const val TYPE_MARKED = 0x0002
  }
}