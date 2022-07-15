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
import android.view.View

/**
 * Create by KunMinX at 19/6/15
 */
class CommonViewPagerAdapter(enableDestroyItem: Boolean, title: Array<String>) : PagerAdapter() {
  private val count: Int
  private val enableDestroyItem: Boolean
  private val title: Array<String>
  override fun getCount(): Int {
    return count
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view === `object`
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    return container.getChildAt(position)
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    if (enableDestroyItem) {
      container.removeView(`object` as View)
    }
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return title[position]
  }

  init {
    count = title.size
    this.enableDestroyItem = enableDestroyItem
    this.title = title
  }
}