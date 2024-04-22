package com.xinhua.language.wanbang.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.xinhua.language.R
import com.xinhua.language.databinding.GuideItemBinding

/**
author:zhoujingjin
date:2022/11/19
 */
class GuideAdapter(val nextClick:NextClickCallBack):
    BaseQuickAdapter<Int, BaseDataBindingHolder<GuideItemBinding>>(R.layout.guide_item) {
    override fun convert(holder: BaseDataBindingHolder<GuideItemBinding>, item: Int) {
        val position = getItemPosition(item)
        holder.dataBinding?.apply {
            ivImage.setImageResource(item)
            ivImage.setOnClickListener {
                nextClick.clickNext(position)
            }
        }
    }
}
interface NextClickCallBack{
    fun clickNext(position:Int)
}
