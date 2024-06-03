package com.xinhua.language.wanbang.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.permissionx.guolindev.PermissionX
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentWriteBinding
import com.xinhua.language.wanbang.adapter.PdfAdapter

class WriteFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): WriteFragment {
            return WriteFragment()
        }
    }
    private var binding:FragmentWriteBinding? = null
    private lateinit var adapter:PdfAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_write,null)
        binding = DataBindingUtil.bind(view)
        return view
    }
    private val PICK_PDF_FILE = 2
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            ivAdd.setOnClickListener {
                PermissionX.init(this@WriteFragment)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            openPdfFilePicker()
                        } else {
                        }
                    }
            }
            rv.layoutManager = LinearLayoutManager(context)
            adapter = PdfAdapter{
                updateHistoryList()
            }
            rv.adapter = adapter
        }
        updateHistoryList()
    }

    private fun openPdfFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf"))
        }
        startActivityForResult(intent, PICK_PDF_FILE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                savePdfUri(uri.toString())
                updateHistoryList()
                val intent = Intent(context, PdfViewerActivity::class.java).apply {
                    putExtra("pdfUri", uri.toString())
                }
                startActivity(intent)
            }
        }
    }
    private fun savePdfUri(uri: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val historySet = sharedPreferences.getStringSet("pdf_uris", mutableSetOf())?.toMutableSet()
        historySet?.add(uri)
        editor.putStringSet("pdf_uris", historySet)
        editor.apply()
    }
    private fun loadPdfUris(): MutableList<String> {
        val sharedPreferences = requireActivity().getSharedPreferences("pdf_history", Context.MODE_PRIVATE)
        val historySet = sharedPreferences.getStringSet("pdf_uris", mutableSetOf())?.toMutableList()
        return historySet ?: mutableListOf()
    }

    private var pdfList = mutableListOf<String>()
    private fun updateHistoryList() {
        pdfList.clear()
        pdfList.addAll(loadPdfUris())
        adapter.setList(pdfList)
    }
}