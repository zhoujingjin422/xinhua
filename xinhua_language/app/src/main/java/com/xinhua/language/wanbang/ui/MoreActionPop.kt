package com.xinhua.language.wanbang.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.CalendarContract.Colors
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import com.xinhua.language.R
import com.xinhua.language.databinding.PopMoreActionBinding
import razerdp.basepopup.BasePopupWindow
import java.io.File

class MoreActionPop(context: Context, uri: Uri, private val action: () -> Unit) :
    BasePopupWindow(context) {
    init {
        setContentView(R.layout.pop_more_action)
        val bind = DataBindingUtil.bind<PopMoreActionBinding>(contentView)
        bind?.apply {
            tvDelete.setOnClickListener {
                deletePdfUri(uri.toString())
                dismiss()
            }
            tvRename.setOnClickListener {
                WordInputPop(context, "") {
                    renamePdfUri(uri.toString(), it)
                    action.invoke()
                }.showPopupWindow()
                dismiss()
            }
            tvShare.setOnClickListener {
                sharePdfUri(uri)
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun deletePdfUri(uri: String) {
        val sharedPreferences = context.getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val historySet = sharedPreferences.getStringSet("pdf_uris", mutableSetOf())?.toMutableSet()
        historySet?.removeIf { it == uri }
        editor.putStringSet("pdf_uris", historySet)
        editor.apply()
        action.invoke()
    }

    private fun sharePdfUri(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(Intent.createChooser(shareIntent, "分享"))
    }

    private fun renamePdfUri(oldUri: String, newName: String) {
        val sharedPreferences = context.getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val historySet = sharedPreferences.getStringSet("pdf_uris", mutableSetOf())?.toMutableSet()
        var older = oldUri
        if(oldUri.contains("|")){
            older = oldUri.split("|")[1]
        }
        val newHistorySet = historySet?.map {
            val parts = it.split("|")
            if (parts.size>1){
                if (parts[1] == older) "$newName|$older" else it
            }else{
                if (it==older){
                    "$newName|$older"
                }else{
                    it
                }
            }
        }?.toMutableSet()
        editor.putStringSet("pdf_uris", newHistorySet)
        editor.apply()
    }
}