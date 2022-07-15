/*
 *
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
 *
 */
package com.kunminx.architecture.data.response

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

/**
 * TODO: 专用于数据层返回结果至 domain 层或 ViewModel，原因如下：
 *
 *
 * liveData 专用于页面开发、解决生命周期安全问题，
 * 有时数据并非通过 liveData 分发给页面，也可是通过其他方式通知非页面组件，
 * 这时 repo 方法中内定通过 liveData 分发便不合适，不如一开始就规定不在数据层通过 liveData 返回结果。
 *
 *
 * 如这么说无体会，详见《如何让同事爱上架构模式、少写 bug 多注释》解析
 * https://xiaozhuanlan.com/topic/8204519736
 *
 *
 * Create by KunMinX at 2020/7/20
 */
class DataResult<T> {
  val result: T
  val responseStatus: ResponseStatus

  constructor(entity: T, responseStatus: ResponseStatus) {
    result = entity
    this.responseStatus = responseStatus
  }

  constructor(entity: T) {
    result = entity
    responseStatus = ResponseStatus()
  }

  interface Result<T> {
    fun onResult(dataResult: DataResult<T>?)
  }
}