package com.xinhua.language.wanbang.ui

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityPdfViewerBinding
import com.xinhua.language.wanbang.BaseVMActivity
import java.io.File
import java.io.IOException

/**
author:zhoujingjin
date:2024/5/31
 */
class PdfViewerActivity: BaseVMActivity() {
    private val binding by binding<ActivityPdfViewerBinding>(R.layout.activity_pdf_viewer)

    override fun initView() {
        val pdfUri = intent.getStringExtra("pdfUri")
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { finish() }
        pdfUri?.let {
            if (it.contains("|")){
                binding.toolBar.title = it.split("|")[0]
                loadPdf(Uri.parse(it.split("|")[1]))
            }else{
                binding.toolBar.title = getFileNameFromUri(Uri.parse(it))
                loadPdf(Uri.parse(it))
            }
        }
    }
   private fun getFileNameFromUri(uri: Uri): String {
        var fileName = "Unknown"
        val cursor = contentResolver.query(uri, null, null, null, null)
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
    private fun loadPdf(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.let {
                binding.pdfView.fromStream(it)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .onLoad { inputStream.close() } // Close the stream after loading
                    .onError { t ->
                        Toast.makeText(this, "Error loading PDF: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                    .load()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to open PDF file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun initData() {
    }
}