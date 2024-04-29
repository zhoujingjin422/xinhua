package com.xinhua.language.wanbang.utils

import android.app.Activity
import android.os.AsyncTask
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xinhua.language.wanbang.bean.WechatConent
import org.json.JSONObject

class PaymentUtil(private val activity: Activity) {

    // 微信支付
    //appId: String, partnerId: String, prepayId: String, nonceStr: String, timeStamp: String, packageValue: String, sign: String)
    fun payWithWeChat(wechatConent: WechatConent) {
        val req = PayReq()
        req.appId = wechatConent.appid
        req.partnerId = wechatConent.partnerid
        req.prepayId = wechatConent.prepayid
        req.nonceStr = wechatConent.noncestr
        req.timeStamp = wechatConent.timestamp
        req.packageValue = wechatConent.`package`
        req.sign = wechatConent.sign
        val api = WXAPIFactory.createWXAPI(activity, null)
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
                Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show()
                // 支付成功后的操作
            } else {
                Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show()
                // 支付失败后的操作
            }
        }
    }
}
