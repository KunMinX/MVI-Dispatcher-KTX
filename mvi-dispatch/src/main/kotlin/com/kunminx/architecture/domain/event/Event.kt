package com.kunminx.architecture.domain.event

/**
 * Create by KunMinX at 2022/6/16
 */
open class Event<P, R> {
  var eventId = 0
  var param: P? = null
  var result: R? = null
}