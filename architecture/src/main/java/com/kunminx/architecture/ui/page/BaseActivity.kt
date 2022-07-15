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
package com.kunminx.architecture.ui.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.scope.ViewModelScope
import com.kunminx.architecture.utils.AdaptScreenUtils
import com.kunminx.architecture.utils.Utils

/**
 * Create by KunMinX at 19/8/1
 */
abstract class BaseActivity : AppCompatActivity() {
  private val mViewModelScope = ViewModelScope()
  protected abstract fun onInitViewModel()
  protected abstract fun onInitView()
  protected fun onInitData() {}
  protected open fun onOutput() {}
  protected open fun onInput() {}

  @SuppressLint("SourceLockedOrientationActivity")
  override fun onCreate(savedInstanceState: Bundle?) {
    transparentStatusBar(this)
    super.onCreate(savedInstanceState)
    onInitViewModel()
    onInitView()
    onInitData()
    onOutput()
    onInput()
  }

  //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
  //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
  //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
  //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。
  //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840
  protected fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
    return mViewModelScope.getActivityScopeViewModel(this, modelClass)
  }

  protected fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
    return mViewModelScope.getApplicationScopeViewModel(modelClass)
  }

  override fun getResources(): Resources {
    return if (Utils.app!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    ) {
      AdaptScreenUtils.adaptWidth(super.getResources(), 360)
    } else {
      AdaptScreenUtils.adaptHeight(super.getResources(), 640)
    }
  }

  protected fun toggleSoftInput() {
    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
  }

  protected fun openUrlInBrowser(url: String?) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
  }

  companion object {
    private const val STATUS_BAR_TRANSPARENT_COLOR = 0x00000000
    fun transparentStatusBar(activity: Activity) {
      val window = activity.window
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      val vis = window.decorView.systemUiVisibility
      window.decorView.systemUiVisibility = option or vis
      window.statusBarColor = STATUS_BAR_TRANSPARENT_COLOR
    }
  }
}