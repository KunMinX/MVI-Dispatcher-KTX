package com.kunminx.architecture.ui.adapter

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
import java.util.ArrayList

/**
 * Create by KunMinX at 2020/6/24
 */
abstract class BaseAdapter<T, V : ViewBinding?> : RecyclerView.Adapter<BaseHolder<V>>() {
  private val data: MutableList<T>
  protected var listener: OnItemClickListener<T>? = null
  fun setData(data: List<T>?) {
    this.data.clear()
    this.data.addAll(data!!)
    notifyDataSetChanged()
  }

  fun setListener(listener: OnItemClickListener<T>?) {
    this.listener = listener
  }

  protected fun getData(): List<T> {
    return data
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseHolder<V> {
    return BaseHolder(onBindingView(viewGroup))
  }

  override fun onBindViewHolder(holder: BaseHolder<V>, position: Int) {
    onBindingData(holder, data[position], position)
  }

  protected abstract fun onBindingData(holder: BaseHolder<V>?, t: T, position: Int)
  protected abstract fun onBindingView(viewGroup: ViewGroup?): V
  override fun getItemCount(): Int {
    return data.size
  }

  class BaseHolder<V : ViewBinding?>(val binding: V) : RecyclerView.ViewHolder(
    binding!!.root
  )

  interface OnItemClickListener<T> {
    fun onItemClick(viewId: Int, position: Int, t: T)
  }

  init {
    data = ArrayList()
  }
}