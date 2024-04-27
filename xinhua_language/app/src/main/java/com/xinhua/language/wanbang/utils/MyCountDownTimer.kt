package com.xinhua.language.wanbang.utils

import android.os.CountDownTimer

class MyCountDownTimer(
    private val totalTimeInMillis: Long,
    private val intervalInMillis: Long,
    private val onTickCallback: (Long) -> Unit,
    private val onFinishCallback: () -> Unit
) : CountDownTimer(totalTimeInMillis, intervalInMillis) {

    override fun onTick(millisUntilFinished: Long) {
        onTickCallback.invoke(millisUntilFinished / 1000)
    }

    override fun onFinish() {
        onFinishCallback.invoke()
    }
}