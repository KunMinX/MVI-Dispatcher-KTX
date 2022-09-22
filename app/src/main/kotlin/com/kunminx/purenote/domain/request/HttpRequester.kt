package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.intent.Api

/**
 * Create by KunMinX at 2022/8/24
 */
class HttpRequester : MviDispatcherKTX<Api>() {
  override suspend fun onHandle(event: Api) {
    when (event) {
      is Api.Loading -> sendResult(event)
      is Api.GetWeatherInfo -> {
        input(Api.Loading(true))
        val result = DataRepository.getWeatherInfo(Api.GET_WEATHER_INFO, event.param)
        if (result.second.isEmpty()) sendResult(event.copy(live = result.first))
        else input(Api.Error(result.second))
        input(Api.Loading(false))
      }
      is Api.Error -> sendResult(event)
    }
  }
}
