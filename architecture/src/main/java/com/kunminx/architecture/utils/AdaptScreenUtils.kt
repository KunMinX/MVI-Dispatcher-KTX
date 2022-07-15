package com.kunminx.architecture.utils

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
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

  fun getAdaptResult(): Resources {
    return if (Utils.app!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      adaptWidth(Utils.app!!.resources, 360)
    } else {
      adaptHeight(Utils.app!!.resources, 640)
    }
  }

  private fun setAppDmXdpi(xdpi: Float) {
    Utils.app!!.resources.displayMetrics.xdpi = xdpi
  }

  private fun getDisplayMetrics(resources: Resources): DisplayMetrics {
    return getMiuiTmpMetrics(resources) ?: return resources.displayMetrics
  }

  private fun getMiuiTmpMetrics(resources: Resources): DisplayMetrics? {
    if (!isInitMiui) {
      var ret: DisplayMetrics? = null
      val simpleName = resources.javaClass.simpleName
      if ("MiuiResources" == simpleName || "XResources" == simpleName) {
        try {
          mTmpMetricsField = Resources::class.java.getDeclaredField("mTmpMetrics")
          mTmpMetricsField!!.setAccessible(true)
          ret = mTmpMetricsField!!.get(resources) as DisplayMetrics
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