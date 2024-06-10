package com.xinhua.language.wanbang.ui

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityPdfViewerBinding
import com.xinhua.language.wanbang.BaseVMActivity


/**
author:zhoujingjin
date:2024/5/31
 */
class PdfViewerActivity: BaseVMActivity() {
    private val binding by binding<ActivityPdfViewerBinding>(R.layout.activity_pdf_viewer)

    override fun initView() {
        val pdfUri = intent.getStringExtra("pdfUri")
        pdfUri?.let {
            if (it.contains("|")){
                binding.toolBar.title = it.split("|")[0]
                loadPdf(Uri.parse(it.split("|")[1]))
            }else{
                binding.toolBar.title = getFileNameFromUri(Uri.parse(it))
                loadPdf(Uri.parse(it))
            }
        }
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { finish() }

    }
   private fun getFileNameFromUri(uri: Uri): String {
       var result: String? = null
       if (uri.scheme.equals("content")) {
           val cursor = contentResolver.query(uri, null, null, null, null)
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
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open PDF file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun initData() {
    }
}