package com.kunminx.architecture.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kunminx.architecture.ui.adapter.BaseAdapter.BaseHolder

/**
 * Create by KunMinX at 2020/6/24
 */
abstract class BaseAdapter<T, V : ViewBinding?> : RecyclerView.Adapter<BaseHolder<V>>() {
  private val data: MutableList<T>
  protected var onItemClick: ((Int, Int, T) -> Unit)? = null
  fun setData(data: List<T>?) {
    this.data.clear()
    this.data.addAll(data!!)
    notifyDataSetChanged()
  }

  fun setListener(onItemClick: (viewId: Int, position: Int, t: T) -> Unit) {
    this.onItemClick = onItemClick
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

  init {
    data = ArrayList()
  }
}