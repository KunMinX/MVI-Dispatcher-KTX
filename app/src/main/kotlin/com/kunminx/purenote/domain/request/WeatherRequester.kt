package com.kunminx.purenote.domain.request

import com.kunminx.architecture.domain.dispatch.MviDispatcherKTX
import com.kunminx.purenote.data.repo.DataRepository
import com.kunminx.purenote.domain.intent.Api

/**
 * TODO tip 1：让 UI 和业务分离，让数据总是从生产者流向消费者
 * UI逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * "领域层组件" 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
 * `
 * 换言之，"领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 * 将来升级到 Jetpack Compose 更是如此，
 *
 * Create by KunMinX at 2022/8/24
 */
class WeatherRequester : MviDispatcherKTX<Api>() {
  /**
   * TODO tip 2：
   * 此为领域层组件，接收发自页面消息，内部统一处理业务逻辑，并通过 sendResult 结果分发。
   * 可在页面中配置作用域，以实现单页面独享或多页面数据共享，
   * `
   * 本组件通过封装，迫使数据只能从 "领域层" 到 "表现层" 单向流动，
   * 消除 “mutable 样板代码 & mutable.emit 误用滥用 & repeatOnLifecycle + SharedFlow 错过时机” 等高频痛点。
   */
  override suspend fun onHandle(intent: Api) {
    when (intent) {
      is Api.Loading -> sendResult(intent)
      is Api.GetWeatherInfo -> {
        input(Api.Loading(true))
        val result = DataRepository.getWeatherInfo(Api.GET_WEATHER_INFO, intent.param)
        if (result.second.isEmpty()) sendResult(intent.copy(live = result.first))
        else input(Api.Error(result.second))
        input(Api.Loading(false))
      }
      is Api.Error -> sendResult(intent)
    }
  }
}
