package com.xinhua.language.wanbang.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.Fragment
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityMainBinding
import com.xinhua.language.wanbang.BaseVMActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseVMActivity() {


    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private  val viewModel by viewModel<MainViewModel>()
    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        binding.apply {
            sfl.setOnClickListener {
                //跳转到搜索也没
                startActivity(
                    Intent(this@MainActivity,SearchListActivity::class.java)
                )
            }
        }
    }



    override fun initData() {

    }


}