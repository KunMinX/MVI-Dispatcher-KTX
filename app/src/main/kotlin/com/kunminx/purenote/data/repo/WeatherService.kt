package com.kunminx.purenote.data.repo

import com.kunminx.purenote.data.bean.Weather
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Create by KunMinX at 2022/8/24
 */
interface WeatherService {
  @GET("weather/{api}")
  suspend fun getWeatherInfo(
    @Path("api") api: String,
    @Query("city") city: String,
    @Query("key") key: String,
  ): Weather
}
