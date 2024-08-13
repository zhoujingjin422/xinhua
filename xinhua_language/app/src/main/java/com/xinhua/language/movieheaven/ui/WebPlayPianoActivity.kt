package com.xinhua.language.movieheaven.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.webkit.JavascriptInterface
import com.xinhua.language.movieheaven.BaseVMActivity


/*** 选择服务界面 */
class WebPlayPianoActivity : BaseVMActivity() {
    private var progressDialog:ProgressDialog?=null
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

    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    override fun initData() {

    }


    override fun onDestroy() {
        super.onDestroy()

    }

    class JavaScriptObject(private val activity: Activity) {
        @JavascriptInterface
        fun goback() {
            activity.finish()
        }
    }

    override fun onBackPressed() {

        super.onBackPressed()

    }
}