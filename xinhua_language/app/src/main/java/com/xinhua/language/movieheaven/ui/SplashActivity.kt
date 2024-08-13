package com.xinhua.language.movieheaven.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.xinhua.language.movieheaven.ads.AdListener
import com.xinhua.language.movieheaven.ads.AdUtils
import com.xinhua.language.R

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

//        }
        //延迟两秒，判断是不是首次进入，首次进入到导航页，不是直接进首页

    }
}