package com.xinhua.language.wanbang.bean

data class WechatConent(
    val appid: String,
    val noncestr: String,
    val `package`: String,
    val partnerid: String,
    val prepayid: String,
    val sign: String,
    val out_trade_no: String,
    val timestamp: String
)