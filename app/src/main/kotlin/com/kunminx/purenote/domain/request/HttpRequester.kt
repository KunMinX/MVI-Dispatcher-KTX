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
   *  本组件通过封装，自动消除 “mutable 样板代码 & mutableSharedFlow.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 高频痛点。
   *  ~
   *  ~
   *  As the 'only credible source', it receives messages sent from the page,
   *  processes the business logic internally, and distributes them through sendResult results.
   *  ~
   *  At the same time, as the adult stage of Single Source of Truth,
   *  automatically eliminates the high-frequency pain spots of "mutable boilerplate code
   *  & mutableSharedFlow.setValue abuse & repeatOnLifecycle + SharedFlow miss result".
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
