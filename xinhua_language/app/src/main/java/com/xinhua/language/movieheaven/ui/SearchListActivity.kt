package com.xinhua.language.movieheaven.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivitySearchListBinding
import com.xinhua.language.movieheaven.BaseVMActivity
import com.xinhua.language.movieheaven.adapter.ItemAdapter
import com.xinhua.language.movieheaven.bean.WebData
import com.xinhua.language.movieheaven.ext.getSpValue
import com.xinhua.language.movieheaven.ext.hideKeyboard
import com.xinhua.language.movieheaven.ext.putSpValue
import com.xinhua.language.movieheaven.ext.showKeyboard
import com.xinhua.language.movieheaven.ext.versionName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
//    private lateinit var webView1:WebView
//    private lateinit var webView2:WebView
    private lateinit var progressDialog:ProgressDialog
    override fun initView() {

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@SearchListActivity)
            adapter = ItemAdapter {item,_->
                if (item.url==getSpValue("url","")+"?app=weike_android&uuid=${getAndroidID()}&version=${versionName}"){
                    putSpValue("showYs",true)
                    startActivityForResult(Intent(this@SearchListActivity,WebPlayActivity::class.java).putExtra("title",item.title).putExtra("url","${item.url}?app=weike_android&uuid=${getAndroidID()}&version=${versionName}"),2222)
                }else
                startActivityForResult(Intent(this@SearchListActivity,WebPlayActivity::class.java).putExtra("title",item.title).putExtra("url",item.url),2222)
            }
            recyclerView.adapter = adapter
            tvSearch.setOnClickListener {
                if (et.text.isEmpty()){
                    return@setOnClickListener
                }
                hideKeyboard()
                progressDialog.show()
                dataList.clear()
                webView.loadUrl("https://www.baidu.com/s?wd=${et.text}")
//                webView1.loadUrl("https://www.baidu.com/s?pn=10&wd=${et.text}")
//                webView2.loadUrl("https://www.baidu.com/s?pn=20&wd=${et.text}")
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
                if (textView.text.isNotEmpty() &&i==EditorInfo.IME_ACTION_SEARCH){
                    hideKeyboard()
                    progressDialog.show()
                    dataList.clear()
                    webView.loadUrl("https://m.baidu.com/s?wd=${et.text}")
//                    webView1.loadUrl("https://www.baidu.com/s?pn=10&wd=${et.text}")
//                    webView2.loadUrl("https://www.baidu.com/s?pn=20&wd=${et.text}")
                     true
                }
               false
            }
            ivDelete.setOnClickListener {
                et.setText("")
            }
            showKeyboard(et)
        }
        progressDialog = ProgressDialog(this)
         webView= WebView(this)
//         webView1= WebView(this)
//         webView2= WebView(this)
        // 启用JavaScript
        webView.settings.javaScriptEnabled = true
//        webView1.settings.javaScriptEnabled = true
//        webView2.settings.javaScriptEnabled = true

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
//        webView1.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                // 页面加载完成后调用JavaScript来提取数据
//                view?.evaluateJavascript(
//                    "(function() { return document.documentElement.outerHTML; })();"
//                ) { html ->
//                    // 在这里解析HTML数据
//                    parseHtml(StringEscapeUtils.unescapeHtml4(html))
//                }
//            }
//        }
//        webView2.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                // 页面加载完成后调用JavaScript来提取数据
//                view?.evaluateJavascript(
//                    "(function() { return document.documentElement.outerHTML; })();"
//                ) { html ->
//                    // 在这里解析HTML数据
//                    parseHtml(StringEscapeUtils.unescapeHtml4(html))
//                }
//            }
//        }
        // 加载百度搜索页面
        // 添加JavaScript接口
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
//        webView1.addJavascriptInterface(WebAppInterface(this), "Android")
//        webView2.addJavascriptInterface(WebAppInterface(this), "Android")

    }

    override fun initData() {
    }
    private var int =0
    private fun parseHtml(html: String) {
        int++
        CoroutineScope(Dispatchers.IO).launch {
            val list = Gson().fromJson<List<String>>(getSpValue("key",""),object: TypeToken<List<String>>(){}.type)
            if (dataList.size==0&&list.contains(binding.et.text.toString()))
            dataList.add(WebData("柠檬影院","柠檬影院为您提供最新电视剧大全免费网站，最新电视剧、热门电影免费在线观看，提供高清版免费下载，影视大全是最好的在线影视免费网站。",getSpValue("url","")+"?app=weike_android&uuid=${getAndroidID()}&version=${versionName}"))
            val decodedHtml = html
                .replace("\\u003C", "<")
                .replace("\\\"", "\"")
            val connect = Jsoup.parse(decodedHtml)
            handleData(connect)
            // 设置移动浏览器的 User-Agent
            // 设置移动浏览器的 User-Agent
            val userAgent =
                "Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36"
           val document = Jsoup.connect("https://m.baidu.com/s?pn=10&wd=${binding.et.text}")
               .userAgent(userAgent).get()
            handleData(document)
//            if (int==3){
                withContext(Dispatchers.Main){
                    progressDialog.dismiss()
                    adapter.setList(dataList)
                }
//            }
        }
    }

    private fun handleData(connect: Document) {
        val results: Elements = connect.getElementsByClass("c-result result")
        val resultsContent: Elements = connect.getElementsByClass("c-result-content")
        results.forEachIndexed { index, element ->
            val linkElement = resultsContent[index]
            val bikeElement = element.getElementsByClass("_no-spacing_eq0t5_4").first()
            val link = linkElement.select("article").first().attr("rl-link-href")
            val tittleElement =
                element.getElementsByClass("_no-spacing_1bhnz_26 cu-line-clamp1").first()
            val descElement = element.getElementsByClass(" c-color summary-gap_3Jb4I").first()
            if (tittleElement != null && link.isNotEmpty()) {
                val title = tittleElement.text()
                if (descElement != null) {
                    val desc = descElement.text()
                    dataList.add(WebData(title, desc, link))
                    println("element$index:Title: $title")
                    println("element$index:Desc: $desc")
                    println("element$index:link: $link")
                    println("element----------------------------------")
                }
                if (bikeElement != null) {
                    val desc = bikeElement.text()
                    dataList.add(WebData(title, desc, link))
                    println("element$index:Title: $title")
                    println("element$index:Desc: $desc")
                    println("element$index:link: $link")
                    println("element----------------------------------")
                }
            }

        }
    }

    class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==2222&&resultCode==1111){
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}