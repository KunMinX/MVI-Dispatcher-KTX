package com.kunminx.purenote

import android.app.Application
import com.kunminx.architecture.utils.Utils

/**
 * Create by KunMinX at 2022/7/3
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    Utils.init(this)
  }
}