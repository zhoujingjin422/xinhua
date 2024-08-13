package com.xinhua.language.movieheaven.utils

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xinhua.language.movieheaven.ads.AdListener
import com.xinhua.language.movieheaven.ads.AdUtils
import com.xinhua.language.movieheaven.AutoClickApplication.Companion.appOpenManager

class AppLifecycleObserver(
    private val context: Context,
    private val listener: AppLifecycleListener
) : DefaultLifecycleObserver {

    private var pauseTime = -1L
    override fun onStart(owner: LifecycleOwner) {
        // 应用进入前台
        if (pauseTime!=-1L&&System.currentTimeMillis()-pauseTime>10000L){
            //显示插屏ad
            AdUtils.getInstance().interstitialAd(appOpenManager?.currentActivity,object :
                AdListener {
                override fun onShow() {

                }

                override fun onClose() {
                }

                override fun reword(b: Boolean) {
                }
            })
        }
        listener.onAppForegrounded()
    }

    override fun onStop(owner: LifecycleOwner) {
        // 应用进入后台
        pauseTime = System.currentTimeMillis()
        listener.onAppBackgrounded()
    }
}

interface AppLifecycleListener {
    fun onAppForegrounded()  // 应用进入前台
    fun onAppBackgrounded()  // 应用进入后台
}