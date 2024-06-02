package com.xinhua.language.wanbang.adapter

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.xinhua.language.R
import com.xinhua.language.databinding.ItemPdfBinding
import com.xinhua.language.wanbang.ui.MoreActionPop
import com.xinhua.language.wanbang.ui.PdfViewerActivity
import java.io.File

class PdfAdapter:BaseQuickAdapter<String, BaseDataBindingHolder<ItemPdfBinding>>(R.layout.item_pdf) {
    override fun convert(holder: BaseDataBindingHolder<ItemPdfBinding>, item: String) {
        holder.dataBinding?.apply {
            tvName.text = getFileNameFromUri(Uri.parse(item))
            sl.setOnClickListener {
                val intent = Intent(context, PdfViewerActivity::class.java).apply {
                    putExtra("pdfUri", item)
                }
                context.startActivity(intent)
            }
            ivMore.setOnClickListener {
                //点击弹pop进行操作
                MoreActionPop(context).showPopupWindow(ivMore)
            }
        }
    }
    private fun getFileNameFromUri(uri: Uri): String {
        var fileName = "Unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}