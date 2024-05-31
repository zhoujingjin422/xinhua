package com.xinhua.language.wanbang.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.xinhua.language.R
import com.xinhua.language.databinding.ItemPdfBinding

class PdfAdapter:BaseQuickAdapter<String, BaseDataBindingHolder<ItemPdfBinding>>(R.layout.item_pdf) {
    override fun convert(holder: BaseDataBindingHolder<ItemPdfBinding>, item: String) {
        holder.dataBinding?.apply {
            tvName.text = item
            ivMore.setOnClickListener {
                //点击弹pop进行操作
            }
        }
    }
}