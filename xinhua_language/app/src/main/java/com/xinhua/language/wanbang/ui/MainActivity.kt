package com.xinhua.language.wanbang.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.ext.getSpValue
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityMainBinding
import com.xinhua.language.wanbang.utils.ActionHelper

class MainActivity : BaseVMActivity() {
    companion object {
        var purchased = false
        var purchaseTime = 0L
        var productId = ""
        const val BUS_TAG_BUY_STATE_PURCHASED = "BUS_TAG_BUY_STATE_PURCHASED"
    }

    private val fragmentList = mutableMapOf<Int, Fragment>()
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private lateinit var viewModel:MainViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        viewModel = MainViewModel()
        binding.apply {
            iv1.setOnClickListener {
                changeViewState(1)
            }
            iv2.setOnClickListener {
                changeViewState(2)
            }
            iv3.setOnClickListener {
                changeViewState(3)
            }
        }
//        inPurchaseUtils = InPurchaseUtils(this)
//        inPurchaseUtils.conListener = object :InPurchaseUtils.ConnectListener{
//            override fun connectSuc() {
//                inPurchaseUtils.queryPurchases()
//            }
//        }
    }

    private fun changeViewState(i: Int) {
        when (i) {
            1 -> {
                binding.iv1.setTextColor(resources.getColor(R.color.black))
                binding.iv1.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_home_selected),null,null)
                binding.iv2.setTextColor(resources.getColor(R.color.c_666666))
                binding.iv2.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_write),null,null)
                binding.iv2.setTextColor(resources.getColor(R.color.c_666666))
                binding.iv3.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_setting),null,null)
            }

            2 -> {
                binding.iv1.setTextColor(resources.getColor(R.color.c_666666))
                binding.iv1.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_home),null,null)
                binding.iv2.setTextColor(resources.getColor(R.color.black))
                binding.iv2.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_write_selected),null,null)
                binding.iv3.setTextColor(resources.getColor(R.color.c_666666))
                binding.iv3.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_setting),null,null)

            }

            3 -> {
                binding.iv1.setTextColor(resources.getColor(R.color.c_b2b2b2))
                binding.iv1.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_home),null,null)
                binding.iv2.setTextColor(resources.getColor(R.color.c_b2b2b2))
                binding.iv2.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_write),null,null)
                binding.iv3.setTextColor(resources.getColor(R.color.black))
                binding.iv3.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.mipmap.icon_setting_selected),null,null)
            }

        }
        changeFragment(i)
    }

    private fun changeFragment(i: Int) {
        val trans = supportFragmentManager.beginTransaction()
        var fragment = fragmentList[i]
        fragmentList.forEach {
            trans.hide(it.value)
        }
        if (fragment == null) {
            fragment = when (i) {
                1 -> {
                    HomeFragment.getInstance()
                }

                2 -> {
                    WriteFragment.getInstance()
                }

                else -> {
                    SettingFragment.getInstance()
                }
            }
            trans.add(R.id.container, fragment).commitAllowingStateLoss()
            fragmentList[i] = fragment
        } else {
            trans.show(fragment).commitAllowingStateLoss()
        }
    }
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun initData() {
        if (!getSpValue("hasShowPrivacy", false)) {
            ServeAndPrivatePop(this).showPopupWindow()
        }
        changeFragment(1)
        ActionHelper.doAction("open")
    }

    override fun onResume() {
        super.onResume()
        networkChangeReceiver = NetworkChangeReceiver(this)
        networkChangeReceiver.register()
    }

    override fun onPause() {
        super.onPause()
        networkChangeReceiver.unregister()
    }
}