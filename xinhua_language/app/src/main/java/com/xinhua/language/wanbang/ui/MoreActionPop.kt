package com.xinhua.language.wanbang.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.CalendarContract.Colors
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
                    renamePdfFile(uri.toString(), it)
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
        historySet?.removeIf { it.split("|")[1] == uri }
        editor.putStringSet("pdf_uris", historySet)
        editor.apply()
        action.invoke()
    }

    private fun sharePdfUri(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
    }

    private fun renamePdfFile(oldUri: String, newName: String): Boolean {
        try {
            val uri = Uri.parse(oldUri)
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            var oldFileName: String? = null
            cursor?.use {
                if (it.moveToFirst()) {
                    oldFileName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                }
            }

            if (oldFileName != null) {
                val oldFile = File(Environment.getExternalStorageDirectory(), oldFileName)
                val newFile = File(Environment.getExternalStorageDirectory(), newName)
                if (oldFile.renameTo(newFile)) {
                    // Update the URI in SharedPreferences
                    val sharedPreferences =
                        context.getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val historySet =
                        sharedPreferences.getStringSet("pdf_uris", mutableSetOf())?.toMutableSet()
                    val newHistorySet = historySet?.map {
                        val parts = it.split("|")
                        if (parts[1] == oldUri) "$newName|${Uri.fromFile(newFile)}" else it
                    }?.toMutableSet()
                    editor.putStringSet("pdf_uris", newHistorySet)
                    editor.apply()
                    action.invoke()
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}