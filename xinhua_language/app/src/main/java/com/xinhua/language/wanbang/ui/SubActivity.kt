package com.xinhua.language.wanbang.ui

import android.text.format.DateUtils
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivitySubBinding
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.bean.DataBean
import com.xinhua.language.wanbang.bean.MessageEvent
import com.xinhua.language.wanbang.bean.WechatConent
import com.xinhua.language.wanbang.bean.WechatPayBean
import com.xinhua.language.wanbang.bean.WechatPayResult
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import com.xinhua.language.wanbang.ext.getSpValue
import com.xinhua.language.wanbang.utils.JsonCallback
import com.xinhua.language.wanbang.utils.PaymentUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Date


class SubActivity:BaseVMActivity() {
    private val binding by binding<ActivitySubBinding>(R.layout.activity_sub)
    private var type = 1
    private var wechatConent:WechatConent? = null
    override fun initView() {
        binding.apply {
            ivClose.setOnClickListener { finish() }
            wechat.isSelected = true
            wechat.setOnClickListener {
                wechat.isSelected = true
                alipay.isSelected = false
                iv1.visibility = View.GONE
                iv2.visibility = View.VISIBLE
                type = 1
            }
            alipay.setOnClickListener {
                wechat.isSelected = false
                alipay.isSelected = true
                iv2.visibility = View.GONE
                iv1.visibility = View.VISIBLE
                type = 2
            }
            flBuy.setOnClickListener {
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

            }
        }
    }
    private fun getWechatPay(){
        val map = mutableMapOf<String,String>()
        map["total_amount"] = "0.01"
        OkGo.post<WechatPayBean>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/pay/wxpay/orderStr") // 请求方式和请求url
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
        OkGo.post<WechatPayResult>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/pay/wxpay/query") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<WechatPayResult>(WechatPayResult::class.java) {
                override fun onSuccess(response: Response<WechatPayResult>) {
                    if (response.body().code==200){
                        if (response.body().data.trade_state=="SUCCESS"){
                            //支付成功，更新一下会员时间
                            updateUserVipDate(response.body().data.transaction_id,response.body().data.success_time)
                        }
                    }
                }
            })
    }
    private fun updateUserVipDate( transactionId:String,time:String){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val date = dateFormat.parse(time)
        date.time+=365*24*60*60*1000L
        val map = mutableMapOf<String,String?>()
//        map["original_transaction_id"] = transactionId
        map["phone"] = getSpValue("userPhone","")
        map["expiredTime"] =dateTimeFormatter1.format(date)
        OkGo.post<DataBean>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/user/updateUserExpiredTime") // 请求方式和请求url
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }
}