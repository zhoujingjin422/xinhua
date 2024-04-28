package com.xinhua.language.wanbang.bean

data class LoginBean(
    val code: Int,
    val `data`: Data,
    val msg: Any
)

data class Data(
    val token: String,
    val user: UserBean
)

