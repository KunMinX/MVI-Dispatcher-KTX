package com.kunminx.architecture.ui.scope

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.kunminx.architecture.ui.scope.ApplicationInstance
import com.kunminx.architecture.domain.queue.FixedLengthList.QueueCallback
import com.kunminx.architecture.domain.queue.FixedLengthList
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.dispatch.MviDispatcher

/**
 * Create by KunMinX at 2022/7/6
 */
class ApplicationInstance private constructor() : ViewModelStoreOwner {
  private var mAppViewModelStore: ViewModelStore? = null
  override fun getViewModelStore(): ViewModelStore {
    if (mAppViewModelStore == null) mAppViewModelStore = ViewModelStore()
    return mAppViewModelStore!!
  }

  companion object {
    val instance = ApplicationInstance()
  }
}