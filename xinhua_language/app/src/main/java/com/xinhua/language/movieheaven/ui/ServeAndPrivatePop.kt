package com.xinhua.language.movieheaven.ui

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.View
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopServePrivateBinding
import com.xinhua.language.movieheaven.ext.putSpValue
import com.xinhua.language.movieheaven.utils.Constant
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

class ServeAndPrivatePop(context: Context, agree:()->Unit) : BasePopupWindow(context) {

    private var str1 =
        "欢迎使用微克浏览器！我们将通过"
    private var str4 = "《隐私政策》"
    private var str5 =
        "帮助你了解我们提供的服务，及收集、处理个人信息的方式。\n" +
                "1.我们可能会申请系统设备权限收集国际移动设备识别码，以及收集其他设备信息如网络设备硬件地址、日志信息，用于识别设备，进行信息推送和安全风控，并申请存储权限，用于下载及缓存相关文件。\n" +
                "2.我们SDK会收集软件列表，设备MAC地址，同时会收集您设备IMEI、IMSI、设备MAC地址、软件安装列表、位置、联系人、通话记录、日历、短信、本机电话号码、图片、音视频、收集读取AndroidID、读取OAID等信息, 用于为您提供更好的软件服务。\n" +
                "3.我们仅通过ip地址 确定“本地”频道中的城市及相关信息，不会收集精确位置信息。\n" +
                "4.上述权限以及存储权限等权限均不会默认或强制开启收集信息。你有权拒绝开启。\n" +
                "5.为实现信息分享、综合统计分析等目的所必需，我们可能会调用传感器并使用与功能相关的最小必要信息（统计参数等）。"

    init {
        setContentView(R.layout.pop_serve_private)
        val bind = DataBindingUtil.bind<PopServePrivateBinding>(contentView)
        bind?.apply {
            tvAgree.setOnClickListener {
                context.putSpValue("hasShowPrivacy", true)
                agree.invoke()
                dismiss()
            }
            tvNot.setOnClickListener {
                dismiss()
                exitProcess(0)
            }
            val spannableString = SpannableString(str1 +  str4 + str5)

//            spannableString.setSpan(object : ClickableSpan() {
//                override fun updateDrawState(ds: TextPaint) {
//                    ds.color = Color.parseColor("#FF1D6AEB")
//                    ds.isUnderlineText = false
//                }
//                override fun onClick(p0: View) {
//                    WebPlayActivity.startActivity(context,"服务协议", Constant.URL_TERMS_OF_USE)
//                }
//            }, str1.length, str1.length + str2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        WebPlayPianoActivity.startActivity(context,"用户隐私协议",Constant.URL_PRIVACY_POLICY)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.parseColor("#FF1D6AEB")
                        ds.isUnderlineText = false
                    }
                },
                str1.length ,
                str1.length  + str4.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                object : UnderlineSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.parseColor("#FF1D6AEB")
                        ds.isUnderlineText = false
                    }
                },
                str1.length,
                str1.length ,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannableString.setSpan(
                object : UnderlineSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.parseColor("#FF1D6AEB")
                        ds.isUnderlineText = false
                    }
                },
                str1.length ,
                str1.length + str4.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            tvContent.movementMethod = LinkMovementMethod.getInstance()
            tvContent.text = spannableString
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }
}