package com.xinhua.language.wanbang.ui

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopWordInputBinding
import razerdp.basepopup.BasePopupWindow

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
                if (etInput.text.toString().endsWith(".pdf"))
                action.invoke(etInput.text.toString())
                else   action.invoke(etInput.text.toString()+".pdf")

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