package com.kunminx.architecture.ui.bind

import android.view.View
import androidx.databinding.BindingAdapter
import android.widget.TextView

/**
 * Create by KunMinX at 2022/8/18
 */
class ClickProxy : View.OnClickListener {
  var listener: View.OnClickListener? = null
  fun setOnClickListener(listener: View.OnClickListener?) {
    this.listener = listener
  }

  override fun onClick(view: View) {
    listener!!.onClick(view)
  }
}
