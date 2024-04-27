package com.xinhua.language.wanbang.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NetworkChangeReceiver(private val activity: AppCompatActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNetworkAvailable(context)) {
            // 网络连接可用
        } else {
            // 没有网络连接
            showNoNetworkDialog()
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showNoNetworkDialog() {
        NetErrorPop(activity).showPopupWindow()
    }

    fun register() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        activity.registerReceiver(this, intentFilter)
    }

    fun unregister() {
        activity.unregisterReceiver(this)
    }
}
