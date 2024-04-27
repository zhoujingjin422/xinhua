package com.xinhua.language.wanbang.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityWebPlayBinding
import com.xinhua.language.wanbang.BaseVMActivity


/*** 选择服务界面 */
class WebPlayActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebPlayBinding>(R.layout.activity_web_play)
    companion object {
        fun startActivity(activity: Context, title: String, url: String) {
            activity.startActivity(
                Intent(activity, WebPlayActivity::class.java)
                    .putExtra("Title", title).putExtra("Url", url)
            )
        }
    }

    override fun initView() {
        binding.apply {
            toolBar.title = intent.getStringExtra("Title")
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                onBackPressed()
            }
            webView.settings.apply {
                javaScriptEnabled = true
            }
            webView.webViewClient = WebViewClient()
        }
    }

    override fun initData() {
        val url = intent.getStringExtra("Url")
        url?.let {
            binding.webView.loadUrl(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.apply {
            stopLoading()
            clearView()
            destroy()
        }
    }

}