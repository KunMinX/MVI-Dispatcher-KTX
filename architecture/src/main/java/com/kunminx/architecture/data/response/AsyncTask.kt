package com.kunminx.architecture.data.response

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by KunMinX at 2022/6/14
 */
object AsyncTask {
  @SuppressLint("CheckResult")
  fun <T : Any> doAction(start: ActionStart<T>, end: ActionEnd<T>) {
    Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<T> -> emitter.onNext(start.data) } as ObservableOnSubscribe<T>)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { t: T -> end.onResult(t) }
  }

  interface ActionStart<T> {
    val data: T
  }

  interface ActionEnd<T> {
    fun onResult(t: T)
  }
}