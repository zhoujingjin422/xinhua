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
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

/**
author:zhoujingjin
date:2023/8/10
 */
class NetErrorPop(context: Context) : BasePopupWindow(context) {


    init {
        setContentView(R.layout.pop_net_error)
        val bind = DataBindingUtil.bind<PopNetErrorBinding>(contentView)
        bind?.apply {
           ivClose.setOnClickListener { dismiss() }
            goSetting.setOnClickListener {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                // 如果你希望直接跳转到移动数据设置页面，可以使用以下代码：
                // val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                dismiss()
            }
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }
}