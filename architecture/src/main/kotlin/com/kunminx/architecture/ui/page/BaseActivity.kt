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

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.kunminx.architecture.utils.AdaptScreenUtils

/**
 * Create by KunMinX at 19/8/1
 */
abstract class BaseActivity : AppCompatActivity() {
  protected open fun onInitView() {}
  protected open fun onInitData() {}
  protected open fun onOutput() {}
  protected open fun onInput() {}

  override fun onCreate(savedInstanceState: Bundle?) {
    transparentStatusBar(this)
    super.onCreate(savedInstanceState)
    onInitView()
    onInitData()
    onOutput()
    onInput()
  }

  override fun getResources(): Resources {
    return AdaptScreenUtils.getAdaptResult()
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