![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vz58k6asj218r0u0jwr.jpg)

&nbsp;

### [🌏 English README](https://github.com/KunMinX/MVI-Dispatcher-KTX/blob/main/README_EN.md)

&nbsp;

上期[《Google Android 官方架构示例，我在起跑线等你》](https://juejin.cn/post/7117498113983512589)侧重拆解官方架构 “领域层” 设计误区，并给出改善建议 —— 通过 MVI-Dispatcher 替代 Event-ViewModel，

然有小伙伴表示，不仅想要 MVI-Dispatcher，还想看看 Kotlin 版 MVI 实践

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48npkl348j214a07gaaq.jpg)

故这期，我们肝个 MVI-Dispatcher-KTX 示例项目，相信查阅后你会耳目一新。

&nbsp;

|                          收藏或置顶                          |                           顺滑转场                           |                           删除笔记                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vup9ck57g20u01o0hbm.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupfbex2g20u01o0qv6.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vuplwiuqg20u01o0x2t.gif) |

&nbsp;

# 项目简介

“单向数据流” 是图形化客户端开发领域最佳实践，

MVI-Dispatcher 通过内聚抹除 “单向数据流” 学习成本，团队新手在不熟 mutable、MVI 情况下，仅根据简明易懂 input-output 设计亦可自动实现 “单向数据流” 开发，

MVI-Dispatcher-KTX 接口及特性与 [MVI-Dispatcher](https://github.com/KunMinX/MVI-Dispatcher) 保持一致，仅因地制宜替换内部实现。

&nbsp;

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch-ktx:4.5.0-beta'
```

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48v3pvrtkj21670q4795.jpg)

&nbsp;

# What‘s More

本项目由 100% Java [MVI-Dispatcher](https://github.com/KunMinX/MVI-Dispatcher) 项目改造而来，

如您正在学习 Kotlin，通过横向对比 MVI-Dispatcher 项目，可快速了解 Android Studio 一键转换后，为因地制宜遵循 Kotlin 特性/风格/思维，我们还可手动完成哪些调整修缮。

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48o423017j210i0u0djm.jpg)

区别于避重就轻实验性示例，MVI-Dispatcher 及 MVI-Dispatcher-KTX 提供完成一款记事本软件最少必要源码实现。

故通过该示例你还可获得内容包括：

> 1.整洁代码风格 & 标准命名规范
>
> 2.对 “视图控制器” 知识点深入理解 & 正确使用
>
> 3.AndroidX 和 Material Design 全面使用
>
> 4.ConstraintLayout 约束布局使用
>
> 5.**十六进制复合状态管理最佳实践**
>
> 6.优秀用户体验 & 交互设计

&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

项目中图标素材来自 [iconfinder](https://www.iconfinder.com/) 提供 **免费授权图片**。

&nbsp;

# Copyright

本项目场景案例及 MVI-Dispatcher-KTX 框架，均属本人独立原创设计，本人对此享有最终解释权。

任何个人或组织，未经与作者本人当面沟通，不得将本项目代码设计及本人对 “唯一可信源” 及 MVI 独家理解用于 "**打包贩卖、出书、卖课**" 等商业用途。

如需引用借鉴 “本项目框架设计背景及思路” 写作发行，请注明**链接出处**。

&nbsp;

# License

```
Copyright 2019-present KunMinX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

