package com.xinhua.language.wanbang.ui

import android.view.View
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivitySubBinding
import com.xinhua.language.wanbang.BaseVMActivity

class SubActivity:BaseVMActivity() {
    private val binding by binding<ActivitySubBinding>(R.layout.activity_sub)
    private var type = 1
    override fun initView() {
        binding.apply {
            ivClose.setOnClickListener { finish() }
            wechat.isSelected = true
            wechat.setOnClickListener {
                wechat.isSelected = true
                alipay.isSelected = false
                iv1.visibility = View.GONE
                iv2.visibility = View.VISIBLE
                type = 1
            }
            alipay.setOnClickListener {
                wechat.isSelected = false
                alipay.isSelected = true
                iv2.visibility = View.GONE
                iv1.visibility = View.VISIBLE
                type = 2
            }
            flBuy.setOnClickListener {
                //开买，调用支付方法
            }
        }
    }

    override fun initData() {
    }
}