package com.xinhua.language.wanbang.bean

data class Amount(
    val currency: String,
    val payer_currency: String,
    val payer_total: Int,
    val total: Int
)