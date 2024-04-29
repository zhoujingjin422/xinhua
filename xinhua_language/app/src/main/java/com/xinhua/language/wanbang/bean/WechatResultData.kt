package com.xinhua.language.wanbang.bean

data class WechatResultData(
    val amount: Amount,
    val appid: String,
    val attach: String,
    val bank_type: String,
    val mchid: String,
    val out_trade_no: String,
    val payer: Payer,
    val promotion_detail: List<Any>,
    val success_time: String,
    val trade_state: String,
    val trade_state_desc: String,
    val trade_type: String,
    val transaction_id: String
)