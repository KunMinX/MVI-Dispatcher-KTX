package com.kunminx.architecture.utils

import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.ui.scope.ViewModelScope
import android.annotation.SuppressLint
import android.os.Bundle
import com.kunminx.architecture.ui.page.BaseActivity
import com.kunminx.architecture.utils.AdaptScreenUtils
import android.app.Activity
import android.app.Application
import android.content.Context
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
import java.lang.NullPointerException
import java.lang.UnsupportedOperationException
import java.lang.reflect.InvocationTargetException

/**
 * <pre>
 * blog  : http://blankj.com
 * time  : 16/12/08
 * desc  : utils about initialization
</pre> *
 */
class Utils private constructor() {
  class FileProvider4UtilCode : FileProvider() {
    override fun onCreate(): Boolean {
      init(context)
      return true
    }
  }

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
        if (app == null) {
          sApplication = applicationByReflect
        } else {
          sApplication = app
        }
      } else {
        if (app != null && app.javaClass != sApplication!!.javaClass) {
          sApplication = app
        }
      }
    }

    val app: Application?
      get() {
        if (sApplication != null) {
          return sApplication
        }
        val app = applicationByReflect
        init(app)
        return app
      }
    private val applicationByReflect: Application
      private get() {
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