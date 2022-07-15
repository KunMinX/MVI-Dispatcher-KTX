package com.kunminx.architecture.utils

import android.annotation.SuppressLint
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Create by KunMinX at 2022/6/30
 */
object TimeUtils {
  const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
  const val YY_MM_DD = "yy-MM-dd"
  const val HH_MM_SS = "HH:mm:ss"

  fun getCurrentTime(time_format: String?): String {
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat(time_format)
    val curDate = Date(System.currentTimeMillis())
    return formatter.format(curDate)
  }

  fun getTime(time: Long, format: String?): String {
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat(format)
    val curDate = Date(time)
    return formatter.format(curDate)
  }

  fun getStringToDate(dateString: String?, pattern: String?): Long {
    @SuppressLint("SimpleDateFormat") val dateFormat = SimpleDateFormat(pattern)
    var date: Date? = null
    try {
      date = dateString?.let { dateFormat.parse(it) } as Date
    } catch (e: ParseException) {
      e.printStackTrace()
    }
    return date!!.time
  }
}