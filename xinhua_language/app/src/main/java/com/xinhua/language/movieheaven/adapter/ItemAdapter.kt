package com.xinhua.language.movieheaven.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.xinhua.language.R
import com.xinhua.language.databinding.ItemLayoutBinding
import com.xinhua.language.movieheaven.bean.WebData

class ItemAdapter(private val action:(item:WebData,index:Int)->Unit):BaseQuickAdapter<WebData, BaseDataBindingHolder<ItemLayoutBinding>>(R.layout.item_layout) {
    override fun convert(holder: BaseDataBindingHolder<ItemLayoutBinding>, item: WebData) {
        holder.dataBinding?.apply {
            tvTitle.text = item.title
            tvDesc.text = item.desc
            tvUrl.text = item.url
            val position = getItemPosition(item)
            if (position==0&&item.title=="柠檬影院"){
                tvTitle.setTextColor(context.resources.getColor(R.color.c_0045b9))
                sll.setBackgroundResource(R.drawable.item_first_back)
            }else{
                tvTitle.setTextColor(context.resources.getColor(R.color.black))
                sll.setBackgroundResource(R.drawable.item_other_back)
            }
            sll.setOnClickListener {
                action.invoke(item,position)
            }
        }
    }

}