package com.xinhua.language.wanbang.utils

import android.app.Activity
import android.os.AsyncTask
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xinhua.language.wanbang.bean.WechatConent
import com.xinhua.language.wanbang.bean.WechatPayResult
import com.xinhua.language.wanbang.ui.SubActivity
import org.json.JSONObject

class PaymentUtil(private val activity: Activity) {

    // 微信支付
    //appId: String, partnerId: String, prepayId: String, nonceStr: String, timeStamp: String, packageValue: String, sign: String)
    fun payWithWeChat(wechatConent: WechatConent) {
        val api = WXAPIFactory.createWXAPI(activity, "wx21555d7b0beb1c03")
        api.registerApp("wx21555d7b0beb1c03")
        val req = PayReq()
        req.appId = wechatConent.appid
        req.partnerId = wechatConent.partnerid
        req.prepayId = wechatConent.prepayid
        req.nonceStr = wechatConent.noncestr
        req.timeStamp = wechatConent.timestamp
        req.packageValue = wechatConent.`package`
        req.sign = wechatConent.sign
        api.sendReq(req)
    }

    // 支付宝支付
    fun payWithAlipay(orderInfo: String) {
        val task = AlipayTask(activity)
        task.execute(orderInfo)
    }

    private class AlipayTask(private val activity: Activity) : AsyncTask<String, Void, Map<String, String>>() {

        override fun doInBackground(vararg params: String): Map<String, String> {
            val alipay = PayTask(activity)
            return alipay.payV2(params[0], true)
        }

        override fun onPostExecute(result: Map<String, String>) {
            super.onPostExecute(result)
            val resultStatus = result["resultStatus"]
            if (resultStatus == "9000") {
                val resultStr = result["result"]
                val json = JSONObject(resultStr)
                if (json.has("alipay_trade_app_pay_response")){
                   val response = json.getJSONObject("alipay_trade_app_pay_response")
                    if (response.has("out_trade_no")){
                        val outTradeNo=  response.getString("out_trade_no")
                        activity as SubActivity
                        activity.checkAliPay(outTradeNo)
                    }
                }
                Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
