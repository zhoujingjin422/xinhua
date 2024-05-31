package com.xinhua.language.wanbang.ui

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopMoreActionBinding
import razerdp.basepopup.BasePopupWindow

class MoreActionPop(context: Context):BasePopupWindow(context) {
   init {
       setContentView(R.layout.pop_more_action)
       val bind = DataBindingUtil.bind<PopMoreActionBinding>(contentView)
       bind?.apply {
           tvDelete.setOnClickListener {

           }
           tvRename.setOnClickListener {

           }
           tvShare.setOnClickListener {

           }
           tvCancel.setOnClickListener {
               dismiss()
           }
       }
   }
}