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
import com.xinhua.language.databinding.PopWordInputBinding
import com.xinhua.language.wanbang.ext.putSpValue
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

/**
author:zhoujingjin
date:2023/8/10
 */
class WordInputPop(context: Context,content:String, private val action:(word:String)->Unit) : BasePopupWindow(context) {

    init {
        setContentView(R.layout.pop_word_input)
        val bind = DataBindingUtil.bind<PopWordInputBinding>(contentView)
        bind?.apply {
            tvConfirm.setOnClickListener {
                dismiss()
                action.invoke(etInput.text.toString())
            }
            etInput.setText(content)
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
        setKeyboardAdaptive(true)
    }
}