![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vz58k6asj218r0u0jwr.jpg)

&nbsp;

### [🌏 English README](https://github.com/KunMinX/MVI-Dispatcher-KTX/blob/main/README_EN.md)



上期[《Google Android 官方架构示例，我在起跑线等你》](https://juejin.cn/post/7117498113983512589)侧重拆解官方架构 “领域层” 设计误区，并给出改善建议 —— 通过 MVI-Dispatcher 替代 Event-ViewModel，

然有小伙伴表示，不仅想要 MVI-Dispatcher，还想看看 Kotlin 下 MVI 实践，以对冲各路示例的云里雾里

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48npkl348j214a07gaaq.jpg)

故这期，我们带着 MVI-Dispatcher-KTX 及精心打磨示例项目而来，相信查阅后你会耳目一新。

&nbsp;

|                          收藏或置顶                          |                           顺滑转场                           |                           删除笔记                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vup9ck57g20u01o0hbm.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupfbex2g20u01o0qv6.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vuplwiuqg20u01o0x2t.gif) |

&nbsp;

# 项目简介

本人长期专注 “业务架构” 模式，所开源 [UnPeek-LiveData](https://github.com/KunMinX/UnPeek-LiveData) 等组件已经过 QQ 音乐等月活过亿生产环境历练。

在本案例中，我将为你展示，MVI-Dispatcher-KTX 是如何 **以简驭繁** 将原本 "繁杂易出错" 之消息分发流程，通过 **寥寥几行代码** 轻而易举完成。

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48ol0bwenj219c0q5af0.jpg)

&nbsp;

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch-ktx:4.5.0-beta'
```

&nbsp;

亲爱的 MVI-Dispatcher-KTX，你已是个成熟的 '唯一可信源'，该学会自己去完成以下几点：

> 1.**可彻底消除 mutable 样板代码**，一行不必写
>
> 2.**可连续发送多事件**，契合 MVI 场景需要
>
> 3.**高性能定长队列，随取随用，用完即走，绝不丢失事件**
>
> 4.**可杜绝团队新手滥用** mutableSharedFlow.emit( ) 于 Activity/Fragment
>
> 5.开发者只需关注 input、output 二处，**从唯一入口 input 注入 Event，并于唯一出口 output 观察**
>
> 6.团队新手在不熟 Flow、SharedFlow、mutable、MVI 情况下，仅根据 MVI-Dispatcher 简明易懂 input-output 设计亦可自动实现 “单向数据流” 开发
>
> 7.可无缝整合至 Jetpack MVVM 等模式项目

&nbsp;

# What‘s More

本项目由 100% Java [MVI-Dispatcher](https://github.com/KunMinX/MVI-Dispatcher) 项目转换而来，通过横向对比 MVI-Dispatcher 项目或查阅 git commit 记录 ，可快速了解 Android Studio 一键转换后，为使项目 100% 遵循 Kotlin 风格/思维，我们还需手动完成哪些调整修缮。

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

