package com.xinhua.language.wanbang.ui

import android.graphics.Paint
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivitySubBinding
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.bean.AliPayBean
import com.xinhua.language.wanbang.bean.AliPayResultBean
import com.xinhua.language.wanbang.bean.DataBean
import com.xinhua.language.wanbang.bean.MessageEvent
import com.xinhua.language.wanbang.bean.WechatConent
import com.xinhua.language.wanbang.bean.WechatPayBean
import com.xinhua.language.wanbang.bean.WechatPayResult
import com.xinhua.language.wanbang.ext.clickN
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import com.xinhua.language.wanbang.ext.getSpValue
import com.xinhua.language.wanbang.utils.Constant
import com.xinhua.language.wanbang.utils.Constant.Companion.BASE_URL
import com.xinhua.language.wanbang.utils.Constant.Companion.PRICE
import com.xinhua.language.wanbang.utils.Constant.Companion.TYPE
import com.xinhua.language.wanbang.utils.JsonCallback
import com.xinhua.language.wanbang.utils.PaymentUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat


class SubActivity:BaseVMActivity() {
    private val binding by binding<ActivitySubBinding>(R.layout.activity_sub)
    private var type = 2
    private var wechatConent:WechatConent? = null
    override fun initView() {
        binding.apply {
            ivClose.clickN { finish() }
            wechat.isSelected = true
            alipay.clickN {
                wechat.isSelected = true
                alipay.isSelected = false
                iv1.visibility = View.GONE
                iv2.visibility = View.VISIBLE
                type = 1
            }
            alipay.clickN {
                wechat.isSelected = false
                alipay.isSelected = true
                iv2.visibility = View.GONE
                iv1.visibility = View.VISIBLE
                type = 2
            }
            val paint = Paint()
            paint.isStrikeThruText = true // 设置中划线
            tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // 应用中划线到TextView
            tvServer.clickN {
                WebPlayActivity.startActivity(
                    this@SubActivity,
                    "服务条款",
                    Constant.URL_TERMS_OF_USE
                )
            }
            tvPrivate.clickN {
                WebPlayActivity.startActivity(
                    this@SubActivity,
                    "隐私协议",
                    Constant.URL_PRIVACY_POLICY
                )
            }
            flBuy.clickN {
                //开买，调用支付方法
                buyNow()
            }
        }
    }

   private fun buyNow(){
        when(type){
            1->{
                //微信支付，获取微信订单
                getWechatPay()
            }
            2->{
                getAliPay()
            }
        }
    }
    private fun getWechatPay(){
        val map = mutableMapOf<String,String>()
        map["total_amount"] = PRICE
        map["type"] = TYPE
        OkGo.post<WechatPayBean>(Constant.BASE_URL+"dict_serve/api/pay/wxpay/orderStr") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<WechatPayBean>(WechatPayBean::class.java) {
                override fun onSuccess(response: Response<WechatPayBean>) {
                    if (response.body().code==200){
                        wechatConent = response.body().data
                       PaymentUtil(this@SubActivity).payWithWeChat(wechatConent!!)
                    }
                }
            })
    }
    private fun getAliPay(){
        val map = mutableMapOf<String,String>()
        map["total_amount"] = PRICE
        map["type"] = TYPE
        OkGo.post<AliPayBean>(Constant.BASE_URL+"dict_serve/api/pay/alipay/orderStr") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<AliPayBean>(AliPayBean::class.java) {
                override fun onSuccess(response: Response<AliPayBean>) {
                    if (response.body().code==200){
                       PaymentUtil(this@SubActivity).payWithAlipay(response.body().data)
                    }
                }
            })
    }
    override fun initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        event?.let {
            when(it.code){
                "SUCCESS"->{
                    //后台查订单
                    checkWechatPay()
                }
                "ERROR"->{
                    //支付出错
                    Toast.makeText(this,"支付出错了",Toast.LENGTH_SHORT).show()
                }
                "CANCEL"->{
                    //取消支付
                    Toast.makeText(this,"取消支付",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun checkWechatPay(){
        val map = mutableMapOf<String,String?>()
        map["out_trade_no"] = wechatConent?.out_trade_no
        map["type"] = TYPE
        OkGo.post<WechatPayResult>( Constant.BASE_URL+"dict_serve/api/pay/wxpay/query") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<WechatPayResult>(WechatPayResult::class.java) {
                override fun onSuccess(response: Response<WechatPayResult>) {
                    if (response.body().code==200){
                        if (response.body().data.trade_state=="SUCCESS"){
                            //支付成功，更新一下会员时间
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                            val date = dateFormat.parse(response.body().data.success_time)
                            date.time+=365*24*60*60*1000L
                            updateUserVipDate(response.body().data.transaction_id,dateTimeFormatter1.format(date))
                        }
                    }
                }
            })
    }
    private fun updateUserVipDate( transactionId:String,time:String){

        val map = mutableMapOf<String,String?>()
//        map["original_transaction_id"] = transactionId
        map["phone"] = getSpValue("userPhone","")
        map["expiredTime"] =time
        map["dict_id"] = Constant.DICT_ID
        OkGo.post<DataBean>(Constant.BASE_URL+"dict_serve/api/user/updateUserExpiredTime") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<DataBean>(DataBean::class.java) {
                override fun onSuccess(response: Response<DataBean>) {
                    if (response.body().code==200){
                        //更新成功，通知首页重新获取用户信息
                        EventBus.getDefault().post(MessageEvent("UPDATE_USER"))
                        finish()
                    }
                }
            })
    }
     fun checkAliPay(outTradeNo:String){
        val map = mutableMapOf<String,String?>()
        map["out_trade_no"] = outTradeNo
        map["type"] = TYPE
        OkGo.post<AliPayResultBean>(BASE_URL+"dict_serve/api/pay/alipay/query") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<AliPayResultBean>(AliPayResultBean::class.java) {
                override fun onSuccess(response: Response<AliPayResultBean>) {
                    if (response.body().code==200){
                        if (response.body().data.msg=="Success"){
                            //支付成功，更新一下会员时间
                            val date =dateTimeFormatter1 .parse(response.body().data.sendPayDate)
                            date.time+=365*24*60*60*1000L
                            updateUserVipDate(response.body().data.traceId,dateTimeFormatter1.format(date))
                        }
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
    }
}