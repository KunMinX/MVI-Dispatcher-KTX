package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.event.ApiEvent

/**
 * Create by KunMinX at 2022/8/24
 */
class HttpRequester : MviDispatcherKTX<ApiEvent>() {
  override suspend fun onHandle(event: ApiEvent) {
    when (event) {
      is ApiEvent.GetWeatherInfo -> {
        val result = DataRepository.getWeatherInfo(ApiEvent.GET_WEATHER_INFO, event.cityCode!!)
        if (result.second.isEmpty()) sendResult(event.copy(result.first))
        else input(ApiEvent.Error(result.second))
      }
      is ApiEvent.Error -> sendResult(event)
    }
  }
}
