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
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Create by KunMinX at 19/7/11
 */
abstract class BaseFragment : DataBindingFragment() {
  protected val mActivity by lazy { context as AppCompatActivity }
  protected open fun onInitData() {}
  protected open fun onOutput() {}
  protected open fun onInput() {}

  override fun initViewModel() {
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addOnBackPressed()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    onInitData()
    onOutput()
    onInput()
  }

  protected fun nav(): NavController {
    return NavHostFragment.findNavController(this)
  }

  protected fun toggleSoftInput() {
    val imm = mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
  }

  protected open fun showKeyboard() {
    if (mActivity != null) mActivity.window.decorView.post { toggleSoftInput() }
  }

  protected fun openUrlInBrowser(url: String?) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
  }

  protected val appContext: Context get() = mActivity.applicationContext

  private fun addOnBackPressed() {
    requireActivity().onBackPressedDispatcher.addCallback(
      this,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          if (!onBackPressed()) requireActivity().onBackPressedDispatcher.onBackPressed()
        }
      })
  }

  protected open fun onBackPressed(): Boolean {
    return true
  }
}
