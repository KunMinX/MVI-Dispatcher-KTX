package com.kunminx.architecture.utils

import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.ui.scope.ViewModelScope
import android.annotation.SuppressLint
import android.os.Bundle
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.architecture.utils.AdaptScreenUtils
import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import androidx.recyclerview.widget.RecyclerView
import com.kunminx.architecture.ui.adapter.BaseAdapter.BaseHolder
import androidx.viewpager.widget.PagerAdapter
import com.kunminx.architecture.data.response.AsyncTask.ActionStart
import com.kunminx.architecture.data.response.AsyncTask.ActionEnd
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.kunminx.architecture.data.response.DataResult
import com.kunminx.architecture.data.response.ResultSource
import androidx.core.content.FileProvider
import android.widget.Toast
import android.util.DisplayMetrics
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
      date = dateFormat.parse(dateString) as Date
    } catch (e: ParseException) {
      e.printStackTrace()
    }
    return Objects.requireNonNull(date).time
  }
}