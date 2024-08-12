package com.xinhua.language.wanbang.ui

import android.content.Context
import android.content.Intent
import com.xinhua.language.wanbang.BaseVMActivity


/*** 选择服务界面 */
class WebPlayActivity : BaseVMActivity() {
    companion object {
        fun startActivity(activity: Context, title: String, url: String) {
            activity.startActivity(
                Intent(activity, WebPlayActivity::class.java)
                    .putExtra("Title", title).putExtra("Url", url)
            )
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}