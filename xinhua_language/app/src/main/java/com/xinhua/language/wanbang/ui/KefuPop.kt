package com.xinhua.language.wanbang.ui

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopKefuBinding
import com.xinhua.language.databinding.PopNetErrorBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class KefuPop(context: Context) : BasePopupWindow(context) {
    init {
        setContentView(R.layout.pop_kefu)
        val bind = DataBindingUtil.bind<PopKefuBinding>(contentView)
        bind?.apply {
           ivClose.setOnClickListener { dismiss() }
        }
        setOutSideDismiss(true)
        popupGravity = Gravity.CENTER
    }
}