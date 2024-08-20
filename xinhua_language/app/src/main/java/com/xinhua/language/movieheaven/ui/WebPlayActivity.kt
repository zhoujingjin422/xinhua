package com.xinhua.language.movieheaven.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.anythink.core.api.ATAdConst
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.rewardvideo.api.ATRewardVideoAd
import com.anythink.rewardvideo.api.ATRewardVideoListener
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityWebBinding
import com.xinhua.language.movieheaven.BaseVMActivity
import com.xinhua.language.movieheaven.ads.AdConfig
import com.xinhua.language.movieheaven.ads.AdListener
import com.xinhua.language.movieheaven.utils.AdsMindDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback


/*** 选择服务界面 */
class WebPlayActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebBinding>(R.layout.activity_web)
    private var isFullscreen = false
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // 处理接收到的消息
            when (msg.what) {
                1 ->{
                    progressDialog?.dismiss()
                }
                2 ->{
                    progressDialog?.show()
                }
                else -> super.handleMessage(msg)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // 如果有保存状态，从中恢复 WebView 的状态
            binding.web.restoreState(savedInstanceState)
        } else {
            intent.getStringExtra("url")?.let {url->
                binding.web.loadUrl(url)
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
        initRewardVideo(this)
        progressDialog = ProgressDialog(this)
    }
    override fun onBackPressed() {
        if (progressDialog?.isShowing==true){
            val message = Message.obtain()
            message.what = 2
            handler.sendMessage(message)
            return
        }
        if (binding.web.canGoBack()){
            binding.web.goBack()
            return
        }else{
            super.onBackPressed()
        }
    }
    class JavaScriptObject(private val activity: WebPlayActivity,private val binding:ActivityWebBinding) {
        private var reword = false
        @JavascriptInterface
        fun showReward() {
            AdsMindDialog(activity){
                activity.rewardVideo(activity,object :
                    AdListener {
                    override fun onShow() {

                    }

                    override fun onClose() {
                        if (reword) {
                            reword = false
                            binding.web.evaluateJavascript(
                                "javascript:rewardSuccess()"
                            ) {
                                Log.e("javascript", it)
                            }
                        }

                    }

                    override fun reword(b: Boolean) {
                        reword = b
                    }
                })
            }.show()
        }
    }

    private var mRewardVideoAd: ATRewardVideoAd? = null
    private var loadSuccess = false
    private var progressDialog:ProgressDialog? = null
   private fun initRewardVideo(activity: Activity,show:Boolean = false) {
        mRewardVideoAd = ATRewardVideoAd(activity, AdConfig.激励视频)
        mRewardVideoAd?.setAdListener(object : ATRewardVideoListener {
            override fun onRewardedVideoAdLoaded() {
                Log.e("reword", "onRewardedVideoAdLoaded")
                loadSuccess = true
                if (show){
                    val message = Message.obtain()
                    message.what = 1
                    handler.sendMessage(message)
                    mRewardVideoAd!!.show(activity)
                }
            }

            override fun onRewardedVideoAdFailed(adError: AdError) {
                Log.e("reword", adError.fullErrorInfo)
                if (show){
                    val message = Message.obtain()
                    message.what = 1
                    handler.sendMessage(message)
                    AlertDialog.Builder(activity).apply {
                        setMessage(adError.fullErrorInfo)
                        setPositiveButton("确认"
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create().show()
                }
                loadSuccess = false
            }

            override fun onRewardedVideoAdPlayStart(atAdInfo: ATAdInfo) {
                listener?.onShow()
            }
            override fun onRewardedVideoAdPlayEnd(atAdInfo: ATAdInfo) {}
            override fun onRewardedVideoAdPlayFailed(adError: AdError, atAdInfo: ATAdInfo) {}
            override fun onRewardedVideoAdClosed(atAdInfo: ATAdInfo) {
                val message = Message.obtain()
                message.what = 1
                handler.sendMessage(message)
                listener?.onClose()
                initRewardVideo(activity)
            }
            override fun onRewardedVideoAdPlayClicked(atAdInfo: ATAdInfo) {}
            override fun onReward(atAdInfo: ATAdInfo) {
                listener?.reword(true)
//                initRewardVideo(activity)
            }
        })
        mRewardVideoAd?.load()
//        var userid = "test_userid_001";
//        val userdata = "test_userdata_001"
//        val localMap: MutableMap<String, Any> = HashMap()
//        localMap[ATAdConst.KEY.USER_ID] = userid
//        localMap[ATAdConst.KEY.USER_CUSTOM_DATA] = userdata
//
//        mRewardVideoAd!!.setLocalExtra(localMap)
    }

    private var listener: AdListener?=null
    //激励视频
    fun rewardVideo(activity: Activity?, listener: AdListener) {
        this.listener = listener
        val message = Message.obtain()
        message.what = 2
        handler.sendMessage(message)
        if (loadSuccess) {
            mRewardVideoAd!!.show(activity)
        } else {
            initRewardVideo(activity!!,true)
        }
    }

}