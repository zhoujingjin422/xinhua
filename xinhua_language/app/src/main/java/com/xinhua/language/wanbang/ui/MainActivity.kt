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

    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
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
            iv4.setOnClickListener {
                changeViewState(4)
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
//        when (i) {
//            1 -> {
//                binding.iv1.setImageResource(R.drawable.home_true)
//                binding.iv2.setImageResource(R.drawable.listen_false)
//                binding.iv3.setImageResource(R.drawable.res_false)
//                binding.iv4.setImageResource(R.drawable.mine_false)
//            }
//
//            2 -> {
//                binding.iv1.setImageResource(R.drawable.home_false)
//                binding.iv2.setImageResource(R.drawable.listen_true)
//                binding.iv3.setImageResource(R.drawable.res_false)
//                binding.iv4.setImageResource(R.drawable.mine_false)
//            }
//
//            3 -> {
//                binding.iv1.setImageResource(R.drawable.home_false)
//                binding.iv2.setImageResource(R.drawable.listen_false)
//                binding.iv3.setImageResource(R.drawable.res_true)
//                binding.iv4.setImageResource(R.drawable.mine_false)
//            }
//
//            4 -> {
//                binding.iv1.setImageResource(R.drawable.home_false)
//                binding.iv2.setImageResource(R.drawable.listen_false)
//                binding.iv3.setImageResource(R.drawable.res_false)
//                binding.iv4.setImageResource(R.drawable.mine_true)
//            }
//        }
        changeFragment(i)
    }

    private fun changeFragment(i: Int) {
//        val trans = supportFragmentManager.beginTransaction()
//        var fragment = fragmentList[i]
//        fragmentList.forEach {
//            trans.hide(it.value)
//        }
//        if (fragment == null) {
////            fragment = when (i) {
////                1 -> {
////                    WebFragment.getInstance()
////                }
////
////                2 -> {
////                    ListenFragment.getInstance()
////                }
////
////                3 -> {
////                    ResourceFragment.getInstance()
////                }
////
////                else -> {
////                    MineFragment.getInstance()
////                }
//            }
//            trans.add(R.id.container, fragment).commitAllowingStateLoss()
//            fragmentList[i] = fragment
//        } else {
//            trans.show(fragment).commitAllowingStateLoss()
//        }
    }

    override fun initData() {
        if (!getSpValue("hasShowPrivacy", false)) {
//            ServeAndPrivatePop(this).showPopupWindow()
        }
        changeFragment(1)
        ActionHelper.doAction("open")
    }
}