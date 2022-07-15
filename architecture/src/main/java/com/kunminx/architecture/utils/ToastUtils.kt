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

/**
 * Create by KunMinX at 2021/8/19
 */
object ToastUtils {
  fun showLongToast(text: String?) {
    Toast.makeText(Utils.Companion.getApp().getApplicationContext(), text, Toast.LENGTH_LONG).show()
  }

  fun showShortToast(text: String?) {
    Toast.makeText(Utils.Companion.getApp().getApplicationContext(), text, Toast.LENGTH_SHORT)
      .show()
  }
}