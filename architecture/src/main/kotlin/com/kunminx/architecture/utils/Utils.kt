package com.kunminx.architecture.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.InvocationTargetException

/**
 * <pre>
 * blog  : http://blankj.com
 * time  : 16/12/08
 * desc  : utils about initialization
</pre> *
 */
class Utils private constructor() {
  companion object {
    @SuppressLint("StaticFieldLeak")
    private var sApplication: Application? = null
    fun init(context: Context?) {
      if (context == null) {
        init(applicationByReflect)
        return
      }
      init(context.applicationContext as Application)
    }

    fun init(app: Application?) {
      if (sApplication == null) {
        if (app == null) sApplication = applicationByReflect else sApplication = app
      } else if (app != null && app.javaClass != sApplication!!.javaClass) {
        sApplication = app
      }
    }

    val app: Application?
      get() {
        if (sApplication != null) return sApplication
        val app = applicationByReflect
        init(app)
        return app
      }
    private val applicationByReflect: Application
      get() {
        try {
          @SuppressLint("PrivateApi") val activityThread =
            Class.forName("android.app.ActivityThread")
          val thread = activityThread.getMethod("currentActivityThread").invoke(null)
          val app = activityThread.getMethod("getApplication").invoke(thread)
            ?: throw NullPointerException("u should init first")
          return app as Application
        } catch (e: NoSuchMethodException) {
          e.printStackTrace()
        } catch (e: IllegalAccessException) {
          e.printStackTrace()
        } catch (e: ClassNotFoundException) {
          e.printStackTrace()
        } catch (e: InvocationTargetException) {
          e.printStackTrace()
        }
        throw NullPointerException("u should init first")
      }
  }

  init {
    throw UnsupportedOperationException("u can't instantiate me...")
  }
}