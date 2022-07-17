![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48wrrwn0oj21890u00y9.jpg)

&nbsp;

The last issue of ["Google Android Official Architecture Example, I'm waiting for you at the starting line"](https://medium.com/@kunminx/google-android-official-architecture-example-i-am-waiting-for-you-at-the-starting-line-b4752d97f283) focused on dismantling the design misunderstandings of the "domain layer" of the official architecture, and gave suggestions for improvement - replacing Event-ViewModel with MVI-Dispatcher,

However, some friends said that they not only want MVI-Dispatcher, but also want to see the MVI practice under Kotlin to hedge the fog of various examples.

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48npkl348j214a07gaaq.jpg)

Therefore, in this issue, we come with MVI-Dispatcher-KTX and carefully polished example projects, I believe you will be refreshed after reading.

&nbsp;

|                      Collect or topped                       |                      Smooth transition                       |                         Delete notes                         |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vup9ck57g20u01o0hbm.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupfbex2g20u01o0qv6.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vuplwiuqg20u01o0x2t.gif) |

&nbsp;

# Project Description

I have been focusing on the “business architecture” model for a long time, and the open source components such as [UnPeekLiveData](https://github.com/KunMinX/UnPeek-LiveData) have been experienced in production environments such as QQ Music with over 100 million monthly lives.

In this case, I will show you how MVI-Dispatcher can simplify the otherwise “complicated and error-prone” message distribution process with just a few lines of code.

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48v3pvrtkj21670q4795.jpg)

&nbsp;

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch-ktx:5.0.0-beta'
```

&nbsp;

MVI-Dispatcher is applicable to Kotlin, through which,

> 1.Can completely eliminate mutable boilerplate code, no need to write one line
>
> 2.Multiple events can be sent continuously to support MVI scene
>
> 3.High performance fixed length queue, use as you go, run out of it, and never lose events
>
> 4.It can prevent team newbies from indiscriminately using mutableSharedFlow.emit( ) in Activity/Fragment
>
> 5.Developers only need to pay attention to input and output, inject events from the unique entry input (), and observe at the unique exit output ()
>
> 6.If the new team members are not familiar with Flow, SharedFlow, mutable and MVI, they can also automatically realize the development of “one-way data flow” based on the simple and easy-to-understand I/O design of MVI-Dispatcher “single entry + single exit”
>
> 7.It can be seamlessly integrated into jetpack MVVM and other mode projects

&nbsp;

# What‘s More

This project is converted from the 100% Java [MVI-Dispatcher](https://github.com/KunMinX/MVI-Dispatcher) project. By comparing the MVI-Dispatcher project horizontally or checking the git commit records, you can quickly understand that after the one-click conversion of Android Studio, in order to make the project 100% follow the Kotlin style/thinking, we also What adjustments need to be done manually.

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h48o423017j210i0u0djm.jpg)

Different from the experimental examples, MVI-Dispatcher and MVI-Dispatcher-KTX provide the minimum necessary source code implementation to complete a notepad software.

Therefore, through this example, you can also obtain content including:

> 1.Clean code style & standard naming conventions
>
> 2.In-depth understanding of “view controller” knowledge points & correct use
>
> 3.Full use of AndroidX and Material Design
>
> 4.ConstraintLayout Constraint Layout Best Practices
>
> 5.Best Practices for Hex Compound State Management
>
> 6.Excellent User Experience & Interaction Design

&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

The icon material in the project comes from [iconfinder](https://www.iconfinder.com/) provided free licensed images.

&nbsp;

# Copyright

The scene cases and MVI dispatcher framework of this project are all my independent original designs, and I have the final right to interpret them.

If you need to quote and use the "background and ideas of the framework design of this project" for writing and publishing, please indicate the source of the link.

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

