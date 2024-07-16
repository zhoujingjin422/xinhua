package com.xinhua.language.wanbang.ui

import com.xinhua.language.R
import com.xinhua.language.databinding.AccountSafeActivityBinding
import com.xinhua.language.databinding.ActivityMainBinding
import com.xinhua.language.wanbang.BaseVMActivity

/**
author:zhoujingjin
date:2024/6/9
 */
class AccountSafeActivity:BaseVMActivity() {
    private val binding by binding<AccountSafeActivityBinding>(R.layout.account_safe_activity)

    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                onBackPressed()
            }
            sb.setOnClickListener {
                UnBindAccountPop(this@AccountSafeActivity){
                    finish()
                }.showPopupWindow()
            }
        }

    }

    override fun initData() {
    }
}