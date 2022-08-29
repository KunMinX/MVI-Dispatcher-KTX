package com.kunminx.architecture.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by KunMinX at 2022/8/20
 */
abstract class BaseBindingAdapter<M, B : ViewDataBinding>(var _list: MutableList<M>) :
  RecyclerView.Adapter<BaseBindingAdapter.BaseBindingViewHolder>() {

  protected var itemClick: ((Int, M, Int) -> Unit)? = null
  protected var itemLongClick: ((Int, M, Int) -> Unit)? = null

  fun setOnItemClick(onItemClick: (viewId: Int, item: M, position: Int) -> Unit) {
    this.itemClick = onItemClick
  }

  fun setOnItemLongClick(onItemLongClick: (viewId: Int, item: M, position: Int) -> Unit) {
    itemLongClick = onItemLongClick
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
    val binding = DataBindingUtil.inflate<B>(
      LayoutInflater.from(parent.context),
      getLayoutResId(viewType),
      parent,
      false
    )
    val holder = BaseBindingViewHolder(binding!!.root)
    holder.itemView.setOnClickListener { v ->
      val position = holder.bindingAdapterPosition
      itemClick?.invoke(holder.itemView.id, _list[position], position)
    }
    holder.itemView.setOnLongClickListener { v ->
      val position = holder.bindingAdapterPosition
      itemLongClick?.invoke(holder.itemView.id, _list[position], position)
      return@setOnLongClickListener true
    }
    return holder
  }

  override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
    val binding = DataBindingUtil.getBinding<B>(holder.itemView)
    onBindItem(binding!!, _list[position], holder)
    binding.executePendingBindings()
  }

  @LayoutRes
  protected abstract fun getLayoutResId(viewType: Int): Int

  protected abstract fun onBindItem(binding: B, item: M, holder: RecyclerView.ViewHolder)

  val list: List<M>
    get() = _list

  fun refresh(list: List<M>) {
    _list.clear()
    _list.addAll(list)
    notifyDataSetChanged()
  }

  fun append(list: List<M>) {
    _list.addAll(list)
    notifyDataSetChanged()
  }

  class BaseBindingViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView)
}
