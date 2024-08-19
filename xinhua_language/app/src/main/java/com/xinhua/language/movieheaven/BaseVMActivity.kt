package com.xinhua.language.movieheaven

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar

abstract class BaseVMActivity : AppCompatActivity() {
    lateinit var mContext: Context
    protected inline fun <reified T : ViewDataBinding> binding(
            @LayoutRes resId: Int
    ): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, resId).apply {
            lifecycleOwner = this@BaseVMActivity
        }
    }
    lateinit var loadingDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
//        loadingDialog = getLoadingDialog(this, null)
        initImmersionBar()
        initView()
        initData()
    }

    abstract fun initView()
    abstract fun initData()

    protected open fun initImmersionBar() {
       immersionBar {
           fitsSystemWindows(false)
           transparentStatusBar()
           autoDarkModeEnable(true)
           statusBarDarkFont(true)
       }
    }
    @SuppressLint("HardwareIds")
    open fun getAndroidID(): String {
        val id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID
        )
        return id ?: ""
    }
}