package com.kunminx.purenote.domain.event

/**
 * Create by KunMinX at 2022/6/16
 */
sealed class ComplexEvent(var _count: Int = 0) {
  data class ResultTest1(var count: Int = 0) : ComplexEvent()
  data class ResultTest2(var count: Int = 0) : ComplexEvent()
  data class ResultTest3(var count: Int = 0) : ComplexEvent()
  data class ResultTest4(var count: Int = 0) : ComplexEvent()
}