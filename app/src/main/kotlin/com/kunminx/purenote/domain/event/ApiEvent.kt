package com.kunminx.purenote.domain.event

import com.kunminx.purenote.data.bean.Weather

/**
 * Create by KunMinX at 2022/8/24
 */
sealed class ApiEvent {
  data class GetWeatherInfo(
    val param: String = CITY_CODE_BEIJING,
    val live: Weather.Live? = null
  ) : ApiEvent()

  data class Error(
    val errorInfo: String? = null
  ) : ApiEvent()

  companion object {
    const val API_KEY = "32d8017dd7b9c2954aa55496a62033c5"
    const val BASE_URL = "https://restapi.amap.com/v3/"
    const val GET_WEATHER_INFO = "weatherInfo"
    const val ERROR = "error"
    const val CITY_CODE_BEIJING = "110000"
  }
}
