package com.xinhua.language.movieheaven.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.xinhua.language.movieheaven.ads.AdListener
import com.xinhua.language.movieheaven.ads.AdUtils
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityWebBinding
import com.xinhua.language.movieheaven.BaseVMActivity


/*** 选择服务界面 */
class WebPlayActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebBinding>(R.layout.activity_web)
    private var isFullscreen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // 如果有保存状态，从中恢复 WebView 的状态
            binding.web.restoreState(savedInstanceState)
        } else {
            intent.getStringExtra("url")?.let {url->
                binding.web.loadUrl("url")
            }
        }
        super.onCreate(savedInstanceState)

    }
    override fun initView() {
        binding.apply {
            tvTitle.text = intent.getStringExtra("title")
            ivBack.setOnClickListener {
                if (web.canGoBack()){
                    web.goBack()
                }else{
                    finish()
                }
            }
            ivHome.setOnClickListener {
                setResult(1111)
                finish()
            }
            web.settings.apply {
                javaScriptEnabled = true
            }
            web.addJavascriptInterface(JavaScriptObject(this@WebPlayActivity,binding),"android")
            web.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                   pb.visibility = View.GONE
                }
            }
            web.webChromeClient = object :WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    pb.progress = newProgress
                }
                private var customView: View? = null
                private var customViewCallback: CustomViewCallback? = null

                override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                    // 隐藏WebView
                    web.visibility = View.GONE

                    // 显示全屏的自定义视图
                    window.decorView as FrameLayout
                    (window.decorView as FrameLayout).addView(
                        view,
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                    customView = view
                    customViewCallback = callback
                    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    isFullscreen = true
                }

                override fun onHideCustomView() {
                    // 恢复WebView的显示
                    if (customView != null) {
                        (window.decorView as FrameLayout).removeView(customView)
                        customView = null
                        web.visibility = View.VISIBLE
                        customViewCallback?.onCustomViewHidden()
                        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        isFullscreen = false
                    }
                }
            }
            WebView.setWebContentsDebuggingEnabled(true)
            intent.getStringExtra("url")?.let {url->
                web.loadUrl(url)
            }
        }

    }
    fun handleOrientationChange() {
        if (isFullscreen) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
    override fun initData() {
    }
    override fun onBackPressed() {
        if (binding.web.canGoBack()){
            binding.web.goBack()
            return
        }else{
            super.onBackPressed()
        }
    }
    class JavaScriptObject(private val activity: Activity,private val binding:ActivityWebBinding) {
        @JavascriptInterface
        fun showReward() {
            AdUtils.getInstance().rewardVideo(activity,object :
                AdListener {
                override fun onShow() {

                }

                override fun onClose() {
                }

                override fun reword(b: Boolean) {
                    if (b){
                        binding.web.evaluateJavascript("javascript:rewardSuccess()"
                        ) {
                            Log.e("javascript",it)
                        }
                    }
                }
            })
        }
    }
}