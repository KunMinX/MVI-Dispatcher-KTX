package com.kunminx.purenote.data.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kunminx.architecture.utils.TimeUtils
import kotlinx.parcelize.Parcelize

/**
 * Created by KunMinX on 2015/7/31.
 */
@Entity
@Parcelize
data class Note(
  @PrimaryKey
  var id: String = "",
  var title: String? = "",
  var content: String? = "",
  @ColumnInfo(name = "create_time")
  var createTime: Long = 0,
  @ColumnInfo(name = "modify_time")
  var modifyTime: Long = 0,
  var type: Int = 0,
) : Parcelable {

  @Ignore
  constructor() : this("")

  @get:Ignore
  val createDate: String
    get() = TimeUtils.getTime(createTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val modifyDate: String
    get() = TimeUtils.getTime(modifyTime, TimeUtils.YYYY_MM_DD_HH_MM_SS)

  @get:Ignore
  val isMarked: Boolean
    get() = type and TYPE_MARKED != 0

  @get:Ignore
  val isTopping: Boolean
    get() = type and TYPE_TOPPING != 0

  @Ignore
  fun toggleType(param: Int) {
    type = if (type and param != 0) {
      type and param.inv()
    } else {
      type or param
    }
  }

  companion object {
    const val TYPE_TOPPING = 0x0001
    const val TYPE_MARKED = 0x0002
  }
}