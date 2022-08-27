package com.kunminx.purenote.data.bean

/**
 * Create by KunMinX at 2022/8/24
 */
data class Weather(
  val status: String? = null,
  val count: String? = null,
  val info: String? = null,
  val infocode: String? = null,
  val lives: List<Live>? = null,
) {
  data class Live(
    val city: String? = null,
    val weather: String? = null,
    val temperature: String? = null,
  )
}
