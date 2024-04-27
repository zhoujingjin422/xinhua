package com.xinhua.language.wanbang.ui

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopServePrivateBinding
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

/**
author:zhoujingjin
date:2023/8/10
 */
class ServeAndPrivatePop(context: Context) : BasePopupWindow(context) {

    private var str1 =
        "感谢您使用双语字典-新华字典双语版，在您使用本软件过程中，我们可能会对您的部分个人信息进行收集、使用和共享。请您仔细阅读"
    private var str2 = "《服务协议》"
    private var str3 = "与"
    private var str4 = "《隐私政策》"
    private var str5 =
        "，并确定完全了解我们对您个人信息的处理規则。如您同意 《服务协议》与《隐私政策》，请点击 “同意”开始使用双语字典-新华字典双语版，我们会尽全力保护您的个人信息安全。"

    init {
        setContentView(R.layout.pop_serve_private)
        val bind = DataBindingUtil.bind<PopServePrivateBinding>(contentView)
        bind?.apply {
            tvAgree.setOnClickListener {
                context.putSpValue("hasShowPrivacy", true)
                dismiss()
            }
            tvNot.setOnClickListener {
                dismiss()
                exitProcess(0)
            }
            val spannableString = SpannableString(str1 + str2 + str3 + str4 + str5)
            spannableString.setSpan(
                context.resources.getColor(R.color.c_0e69c7),
                str1.length,
                str1.length + str2.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    WebPlayActivity.startActivity(context,"服务协议", Constant.URL_TERMS_OF_USE)
                }
            }, str1.length, str1.length + str2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(
                context.resources.getColor(R.color.c_0e69c7),
                str1.length + str2.length + str3.length,
                str1.length + str2.length + str3.length + str4.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        WebPlayActivity.startActivity(context,"用户隐私协议",Constant.URL_PRIVACY_POLICY)
                    }
                },
                str1.length + str2.length + str3.length,
                str1.length + str2.length + str3.length + str4.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvContent.movementMethod = LinkMovementMethod.getInstance()
            tvContent.text = spannableString
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }
}