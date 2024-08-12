package com.xinhua.language.wanbang.ui

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivitySearchListBinding
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.adapter.ItemAdapter
import com.xinhua.language.wanbang.bean.WebData
import com.xinhua.language.wanbang.ext.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
author:zhoujingjin
date:2024/8/12
 */
class SearchListActivity: BaseVMActivity() {
    private val binding by binding<ActivitySearchListBinding>(R.layout.activity_search_list)
    private var dataList = mutableListOf<WebData>()
    private lateinit var adapter:ItemAdapter
    private lateinit var webView:WebView
    private lateinit var progressDialog:ProgressDialog
    override fun initView() {

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@SearchListActivity)
            adapter = ItemAdapter {

            }
            recyclerView.adapter = adapter
            tvSearch.setOnClickListener {
                if (et.text.isEmpty()){
                    return@setOnClickListener
                }
                hideKeyboard()
                progressDialog.show()
                webView.loadUrl("https://www.baidu.com/s?wd=${et.text.toString()}")
            }
            ivBack.setOnClickListener { finish() }
            et.addTextChangedListener {
                if (it.toString().isNotEmpty()){
                    ivDelete.visibility = View.VISIBLE
                }else{
                    ivDelete.visibility = View.GONE
                }
            }
            et.setOnEditorActionListener { textView, i, _ ->
                if (textView.text.isNotEmpty() &&i==EditorInfo.IME_ACTION_GO){
                    hideKeyboard()
                    progressDialog.show()
                    webView.loadUrl("https://www.baidu.com/s?wd=${et.text}")
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            ivDelete.setOnClickListener {
                et.setText("")
            }

        }
        progressDialog = ProgressDialog(this)
         webView= WebView(this)
        // 启用JavaScript
        webView.settings.javaScriptEnabled = true

        // 禁止外部浏览器打开新链接
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // 页面加载完成后调用JavaScript来提取数据
                view?.evaluateJavascript(
                    "(function() { return document.documentElement.outerHTML; })();"
                ) { html ->
                    // 在这里解析HTML数据
                    parseHtml(StringEscapeUtils.unescapeHtml4(html))
                }
            }
        }
        // 加载百度搜索页面
        // 添加JavaScript接口
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

    }

    override fun initData() {
    }
    private fun parseHtml(html: String) {

        CoroutineScope(Dispatchers.IO).launch {
            dataList.clear()
            dataList.add(WebData("影视大全——免费最新最火的电影影电视剧","影视大全为您提供最新电视剧大全免费网站，最新电视剧、热门电影免费在线观看，提供高清版免费下载，影视大全是最好的在线影视免费网站。","https://bywan.mmwcy.cn/"))
            val decodedHtml = html
                .replace("\\u003C", "<")
                .replace("\\\"", "\"")
            val connect = Jsoup.parse(decodedHtml)
            val results: Elements = connect.getElementsByClass("c-result result")
            val resultsContent: Elements = connect.getElementsByClass("c-result-content")
            results.forEachIndexed { index, element ->
                val linkElement = resultsContent[index]
                val bikeElement = element.getElementsByClass("_no-spacing_eq0t5_4").first()
                val link = linkElement.select("article").first().attr("rl-link-href")
                val tittleElement =  element.getElementsByClass("_no-spacing_1bhnz_26 cu-line-clamp1").first()
                val descElement =  element.getElementsByClass(" c-color summary-gap_3Jb4I").first()
                if (tittleElement!=null&&link.isNotEmpty()){
                    val title = tittleElement.text()
                    if (descElement!=null){
                        val desc = descElement.text()
                        dataList.add(WebData(title,desc,link))
                        println("element$index:Title: $title")
                        println("element$index:Desc: $desc")
                        println("element$index:link: $link")
                        println("element----------------------------------")
                    }
                    if (bikeElement!=null){
                        val desc = bikeElement.text()
                        dataList.add(WebData(title,desc,link))
                        println("element$index:Title: $title")
                        println("element$index:Desc: $desc")
                        println("element$index:link: $link")
                        println("element----------------------------------")
                    }
                }

            }
            withContext(Dispatchers.Main){
                progressDialog.dismiss()
                adapter.setList(dataList)
            }
        }
    }
    class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}