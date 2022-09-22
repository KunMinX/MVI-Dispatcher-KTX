package com.kunminx.purenote.domain.intent

/**
 * TODO tip：此 Intent 非传统意义上的 MVI intent，
 *  而是简化 reduce 和 action 后，拍平的 intent，
 *  它可以携带 param，input 至 mvi-Dispatcher，
 *  然后可以 copy 和携带 result，output 至表现层，
 *
 *  具体可参见《解决 MVI 实战痛点》解析
 *  https://juejin.cn/post/7134594010642907149
 *
 * Create by KunMinX at 2022/6/16
 */
sealed class ComplexIntent {
  data class ResultTest1(val count: Int = 0) : ComplexIntent()
  data class ResultTest2(val count: Int = 0) : ComplexIntent()
  data class ResultTest3(val count: Int = 0) : ComplexIntent()
  data class ResultTest4(val count: Int = 0) : ComplexIntent()
}
