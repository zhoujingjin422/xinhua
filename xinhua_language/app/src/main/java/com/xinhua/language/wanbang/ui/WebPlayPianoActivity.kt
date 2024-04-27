package com.xinhua.language.wanbang.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityWebPlayPianoBinding
import com.xinhua.language.wanbang.BaseVMActivity


/*** 选择服务界面 */
class WebPlayPianoActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebPlayPianoBinding>(R.layout.activity_web_play_piano)
    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(
                Intent(activity, WebPlayPianoActivity::class.java)
            )
        }
    }
private var startUrl:String? = null

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView() {
        binding.apply {
            webView.settings.apply {
                useWideViewPort = true
                loadWithOverviewMode = true
                domStorageEnabled = true
                defaultTextEncodingName = "UTF-8"
                allowFileAccess = true
                allowContentAccess = true
                allowFileAccessFromFileURLs = false
                allowUniversalAccessFromFileURLs = false
                javaScriptEnabled = true
            }
            webView.addJavascriptInterface(JavaScriptObject(this@WebPlayPianoActivity),"android")
            webView.webViewClient = object :WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                     startUrl = url
                }
            }
            webView.webChromeClient = object : WebChromeClient(){

            }
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    override fun initData() {
       binding.webView.loadUrl("https://cndicttest.cpdtlp.com.cn/dict/query.html?platform=android")
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webView.apply {
            stopLoading()
            clearView()
            destroy()
        }
    }

    class JavaScriptObject(private val activity: Activity) {
        @JavascriptInterface
        fun goback() {
            activity.finish()
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
            return
        }
        super.onBackPressed()

    }
}