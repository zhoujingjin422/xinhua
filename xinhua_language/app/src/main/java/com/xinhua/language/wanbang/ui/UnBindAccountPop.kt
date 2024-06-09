package com.xinhua.language.wanbang.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
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
import com.xinhua.language.databinding.PopNetErrorBinding
import com.xinhua.language.databinding.PopServePrivateBinding
import com.xinhua.language.databinding.PopUnbindAccountBinding
import com.xinhua.language.wanbang.bean.MessageEvent
import com.xinhua.language.wanbang.ext.clickN
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import org.greenrobot.eventbus.EventBus
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

/**
author:zhoujingjin
date:2023/8/10
 */
class UnBindAccountPop(context: Context,goBack:()->Unit) : BasePopupWindow(context) {
    init {
        setContentView(R.layout.pop_unbind_account)
        val bind = DataBindingUtil.bind<PopUnbindAccountBinding>(contentView)
        bind?.apply {
           ivClose.setOnClickListener { dismiss() }
            goSetting.clickN {
                //解绑
                context.putSpValue("userPhone","")
                context.putSpValue("token","")
                val sharedPreferences = context.getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putStringSet("pdf_uris", mutableSetOf())
                editor.apply()
                EventBus.getDefault().post(MessageEvent("UNBIND"))
                goBack.invoke()
               dismiss()
            }
            sbNot.setOnClickListener {
                dismiss()
            }

        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }
}