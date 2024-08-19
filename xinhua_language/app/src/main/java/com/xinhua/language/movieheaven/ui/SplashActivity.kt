package com.xinhua.language.movieheaven.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xinhua.language.movieheaven.ads.AdListener
import com.xinhua.language.movieheaven.ads.AdUtils
import com.xinhua.language.R
import com.xinhua.language.movieheaven.bean.DataBean
import com.xinhua.language.movieheaven.ext.putSpValue
import com.xinhua.language.movieheaven.ext.versionName
import com.xinhua.language.movieheaven.utils.Constant.Companion.buried_url
import com.xinhua.language.movieheaven.utils.JsonCallback

/**
author:zhoujingjin
date:2022/11/18
 */
class SplashActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        if (!isTaskRoot) {
            finish()
            return
        }
        findViewById<ConstraintLayout>(R.id.parent)
        AdUtils.getInstance().initSplashAdd(this)
        AdUtils.getInstance().initInterstitialAd(this)
        findViewById<ConstraintLayout>(R.id.parent).postDelayed({
            AdUtils.getInstance().splashAd(this,findViewById<ConstraintLayout>(R.id.parent),object :
                AdListener {
                override fun onShow() {

                }

                override fun onClose() {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

                override fun reword(b: Boolean) {
                }

            })
        },1000L)
        open()
//        }
        //延迟两秒，判断是不是首次进入，首次进入到导航页，不是直接进首页

    }



    private fun open(){
        OkGo.post<DataBean<String>>( String(Base64.decode(buried_url.toByteArray(), Base64.DEFAULT), Charsets.UTF_8)) // 请求方式和请求url
            .params("event","app_open")
            .params("uuid",getAndroidID())
            .params("app","weike_android")
            .params("version",versionName)
            .params("bundle_id",packageName)
            .execute(object : JsonCallback<DataBean<String>>(DataBean::class.java) {
                override fun onSuccess(response: Response<DataBean<String>>) {
                    if (response.body().code==200){
                    }
                }
            })
    }
    @SuppressLint("HardwareIds")
    open fun getAndroidID(): String {
        val id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID
        )
        return id ?: ""
    }
}