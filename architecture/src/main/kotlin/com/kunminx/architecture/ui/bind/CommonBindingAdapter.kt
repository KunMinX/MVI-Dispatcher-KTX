/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kunminx.architecture.ui.bind

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Create by KunMinX at 19/9/18
 */
object CommonBindingAdapter {
  @JvmStatic
  @BindingAdapter(value = ["visible"], requireAll = false)
  fun visible(view: View, visible: Boolean) {
    if (visible && view.visibility == View.GONE) {
      view.visibility = View.VISIBLE
    } else if (!visible && view.visibility == View.VISIBLE) {
      view.visibility = View.GONE
    }
  }
  @JvmStatic
  @BindingAdapter(value = ["invisible"], requireAll = false)
  fun invisible(view: View, visible: Boolean) {
    if (visible && view.visibility == View.INVISIBLE) {
      view.visibility = View.VISIBLE
    } else if (!visible && view.visibility == View.VISIBLE) {
      view.visibility = View.INVISIBLE
    }
  }
  @JvmStatic
  @BindingAdapter(value = ["imgRes"], requireAll = false)
  fun setImageResource(imageView: ImageView, imgRes: Int) {
    imageView.setImageResource(imgRes)
  }
  @JvmStatic
  @BindingAdapter(value = ["textColor"], requireAll = false)
  fun setTextColor(textView: TextView, textColorRes: Int) {
    textView.setTextColor(textView.context.getColor(textColorRes))
  }
  @JvmStatic
  @BindingAdapter(value = ["selected"], requireAll = false)
  fun selected(view: View, select: Boolean) {
    view.isSelected = select
  }
  @JvmStatic
  @BindingAdapter(value = ["clipToOutline"], requireAll = false)
  fun clipToOutline(view: View, clipToOutline: Boolean) {
    view.clipToOutline = clipToOutline
  }
  @JvmStatic
  @BindingAdapter(value = ["requestFocus"], requireAll = false)
  fun requestFocus(view: View, requestFocus: Boolean) {
    if (requestFocus) view.requestFocus()
  }
}
