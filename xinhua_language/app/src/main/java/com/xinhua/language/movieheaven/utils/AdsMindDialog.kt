package com.xinhua.language.movieheaven.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.xinhua.language.R

class AdsMindDialog(context: Context,  private val watch:()->Unit) : Dialog(context, R.style.DefaultDialogStyle) {
    init {
        setContentView(R.layout.dialog_ads_mind)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.attributes?.apply {
            gravity = Gravity.CENTER
            width = (getScreenWidth() * 0.8).toInt()
        }
        findViewById<TextView>(R.id.tvAppUpdate).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tvUpdate).setOnClickListener {
            dismiss()
            watch.invoke()
        }

    }
    private fun getScreenWidth(): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }
}