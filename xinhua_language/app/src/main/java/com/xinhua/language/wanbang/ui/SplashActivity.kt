package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.xinhua.language.R
import com.xinhua.language.wanbang.ext.getSpValue

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
        if (!getSpValue("hasShowPrivacy", false)) {
            ServeAndPrivatePop(this) {
                if (getSpValue("First", true)) {
                    startActivity(Intent(this, GuideActivity::class.java))
                } else startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.showPopupWindow()
        }else{
            findViewById<ConstraintLayout>(R.id.parent).postDelayed({

                if (getSpValue("First",true)){
                    startActivity(Intent(this, GuideActivity::class.java))
                }else startActivity(Intent(this, MainActivity::class.java))
                finish()

            },1000L)
        }
        //延迟两秒，判断是不是首次进入，首次进入到导航页，不是直接进首页

    }
}