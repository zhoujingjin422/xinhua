package com.xinhua.language.wanbang.bean

data class WechatPayBean(
    val code: Int,
    val `data`: WechatConent,
    val msg: Any
)