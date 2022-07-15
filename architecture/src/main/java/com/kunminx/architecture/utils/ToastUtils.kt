package com.kunminx.architecture.utils

import android.widget.Toast

/**
 * Create by KunMinX at 2021/8/19
 */
object ToastUtils {
  fun showLongToast(text: String?) {
    Toast.makeText(Utils.app?.applicationContext, text, Toast.LENGTH_LONG).show()
  }

  fun showShortToast(text: String?) {
    Toast.makeText(Utils.app?.applicationContext, text, Toast.LENGTH_SHORT).show()
  }
}