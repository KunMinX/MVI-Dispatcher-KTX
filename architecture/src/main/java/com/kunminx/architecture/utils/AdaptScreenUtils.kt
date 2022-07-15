package com.kunminx.architecture.utils

import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.ui.scope.ViewModelScope
import android.annotation.SuppressLint
import android.os.Bundle
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.architecture.utils.AdaptScreenUtils
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
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
import android.util.Log
import java.lang.Exception
import java.lang.reflect.Field

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/09/23
 * desc  : AdaptScreenUtils
</pre> *
 */
object AdaptScreenUtils {
  private var isInitMiui = false
  private var mTmpMetricsField: Field? = null
  fun adaptWidth(resources: Resources, designWidth: Int): Resources {
    val dm = getDisplayMetrics(resources)
    dm.xdpi = dm.widthPixels * 72f / designWidth
    val newXdpi = dm.xdpi
    setAppDmXdpi(newXdpi)
    return resources
  }

  fun adaptHeight(resources: Resources, designHeight: Int): Resources {
    val dm = getDisplayMetrics(resources)
    dm.xdpi = dm.heightPixels * 72f / designHeight
    val newXdpi = dm.xdpi
    setAppDmXdpi(newXdpi)
    return resources
  }

  private fun setAppDmXdpi(xdpi: Float) {
    Utils.Companion.getApp().getResources().getDisplayMetrics().xdpi = xdpi
  }

  private fun getDisplayMetrics(resources: Resources): DisplayMetrics {
    return getMiuiTmpMetrics(resources)
      ?: return resources.displayMetrics
  }

  private fun getMiuiTmpMetrics(resources: Resources): DisplayMetrics? {
    if (!isInitMiui) {
      var ret: DisplayMetrics? = null
      val simpleName = resources.javaClass.simpleName
      if ("MiuiResources" == simpleName || "XResources" == simpleName) {
        try {
          mTmpMetricsField = Resources::class.java.getDeclaredField("mTmpMetrics")
          mTmpMetricsField.setAccessible(true)
          ret = mTmpMetricsField.get(resources) as DisplayMetrics
        } catch (e: Exception) {
          Log.e("AdaptScreenUtils", "no field of mTmpMetrics in resources.")
        }
      }
      isInitMiui = true
      return ret
    }
    return if (mTmpMetricsField == null) {
      null
    } else try {
      mTmpMetricsField!![resources] as DisplayMetrics
    } catch (e: Exception) {
      null
    }
  }
}