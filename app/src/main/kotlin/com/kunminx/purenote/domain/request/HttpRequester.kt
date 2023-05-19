package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.intent.Api

/**
 * Create by KunMinX at 2022/8/24
 */
class HttpRequester : MviDispatcherKTX<Api>() {
  /**
   * TODO tip 1：
   *  此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   *  可为同业务不同页面复用。
   *  ~
   *  本组件通过封装，默使数据从 "领域层" 到 "表现层" 单向流动，
   *  消除 “mutable 样板代码 & mutable.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 等高频痛点。
   */
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
