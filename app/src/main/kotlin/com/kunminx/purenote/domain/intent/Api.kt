package com.kunminx.purenote.domain.intent

import com.kunminx.purenote.data.bean.Weather

/**
 * Create by KunMinX at 2022/8/24
 */
sealed class Api {
  data class Loading(val isLoading: Boolean) : Api()

  data class GetWeatherInfo(
    val param: String = CITY_CODE_BEIJING,
    val live: Weather.Live? = null
  ) : Api()

  data class Error(
    val errorInfo: String? = null
  ) : Api()

  //TODO 由于有人挪用和滥用本 API_KEY，本账号已被封，所属天气 API 也不再能使用，
  // 如有需要，请自行在 "高德开放平台" 获取和填入

  companion object {
    const val API_KEY = ""
    const val BASE_URL = "https://restapi.amap.com/v3/"
    const val GET_WEATHER_INFO = "weatherInfo"
    const val ERROR = "error"
    const val CITY_CODE_BEIJING = "110000"
  }
}
