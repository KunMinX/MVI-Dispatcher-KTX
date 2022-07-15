package com.kunminx.architecture.data.response

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by KunMinX at 2022/6/14
 */
object AsyncTask {
  @SuppressLint("CheckResult")
  fun <T : Any> doAction(start: () -> T, end: (t: T) -> Unit) {
    Observable.create { emitter -> emitter.onNext(start.invoke()) }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { t: T -> end.invoke(t) }
  }
}