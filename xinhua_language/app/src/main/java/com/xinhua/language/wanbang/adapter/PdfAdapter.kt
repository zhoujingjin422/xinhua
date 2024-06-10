package com.xinhua.language.wanbang.adapter

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.xinhua.language.R
import com.xinhua.language.databinding.ItemPdfBinding
import com.xinhua.language.wanbang.ui.MoreActionPop
import com.xinhua.language.wanbang.ui.PdfViewerActivity
import java.io.File

class PdfAdapter(private val action:()->Unit):BaseQuickAdapter<String, BaseDataBindingHolder<ItemPdfBinding>>(R.layout.item_pdf) {
    override fun convert(holder: BaseDataBindingHolder<ItemPdfBinding>, item: String) {
        holder.dataBinding?.apply {
            if (item.contains("|")){
                tvName.text = item.split("|")[0]
            }else{
                tvName.text = getFileNameFromUri(Uri.parse(item))
            }
            sl.setOnClickListener {
                val intent = Intent(context, PdfViewerActivity::class.java).apply {
                    putExtra("pdfUri", item)
                }
                context.startActivity(intent)
            }
            ivMore.setOnClickListener {
                //点击弹pop进行操作
                if (item.contains("|")){
                    MoreActionPop(context,Uri.parse(item.split("|")[1]),action).showPopupWindow(ivMore)
                }else{
                    MoreActionPop(context,Uri.parse(item),action).showPopupWindow(ivMore)

                }
            }
        }
    }
    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result!!
    }
}