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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kunminx.architecture.ui.scope.ViewModelScope

/**
 * Create by KunMinX at 19/7/11
 */
abstract class BaseFragment : Fragment() {
  private val mViewModelScope = ViewModelScope()
  protected var mActivity: AppCompatActivity? = null
  protected abstract fun onInitViewModel()
  protected abstract fun onInitView(inflater: LayoutInflater, container: ViewGroup?): View?
  protected open fun onInitData() {}
  protected open fun onOutput() {}
  protected open fun onInput() {}

  override fun onAttach(context: Context) {
    super.onAttach(context)
    mActivity = context as AppCompatActivity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    onInitViewModel()
    addOnBackPressed()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return onInitView(inflater, container)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    onInitData()
    onOutput()
    onInput()
  }

  //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
  //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
  //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
  //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。
  //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840
  protected fun <T : ViewModel?> getFragmentScopeViewModel(modelClass: Class<T>): T {
    return mViewModelScope.getFragmentScopeViewModel(this, modelClass)
  }

  protected fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
    return mViewModelScope.getActivityScopeViewModel(mActivity!!, modelClass)
  }

  protected fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
    return mViewModelScope.getApplicationScopeViewModel(modelClass)
  }

  protected fun nav(): NavController {
    return NavHostFragment.findNavController(this)
  }

  protected fun toggleSoftInput() {
    val imm = mActivity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
  }

  protected fun openUrlInBrowser(url: String?) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
  }

  protected val appContext: Context get() = mActivity!!.applicationContext

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