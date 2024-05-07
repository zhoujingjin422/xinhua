package com.xinhua.language.wanbang.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.ext.getSpValue
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityMainBinding
import com.xinhua.language.wanbang.bean.FindUserBean
import com.xinhua.language.wanbang.bean.LoginBean
import com.xinhua.language.wanbang.bean.MessageEvent
import com.xinhua.language.wanbang.bean.UserBean
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.ActionHelper
import com.xinhua.language.wanbang.utils.JsonCallback
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseVMActivity() {
    companion object {
        var purchased = false
        var purchaseTime = 0L
        var productId = ""
        const val BUS_TAG_BUY_STATE_PURCHASED = "BUS_TAG_BUY_STATE_PURCHASED"
    }

    private val fragmentList = mutableMapOf<Int, Fragment>()
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private  val viewModel by viewModel<MainViewModel>()

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
        if (getSpValue("userPhone","").isNotEmpty()){
            findUser()
        }
        changeFragment(1)
        ActionHelper.doAction("open")
    }

    private fun findUser(){
        val map = mutableMapOf<String,String>()
        map["phone"] = getSpValue("userPhone","")
        OkGo.post<FindUserBean>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/user/findUser") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<FindUserBean>(FindUserBean::class.java) {
                override fun onSuccess(response: Response<FindUserBean>) {
                    if (response.body().code==200){
                        viewModel.isLogin.value = true
                        if (!response.body().data.expiredTime.isNullOrEmpty()){
                            if(dateTimeFormatter1.parse(response.body().data.expiredTime).time>System.currentTimeMillis()){
                                viewModel.isVip.postValue(true)
                            }else{
                                viewModel.isVip.postValue(false)
                            }
                        }else{
                            viewModel.isVip.postValue(false)
                        }
                        //获取成功
                        viewModel.user.value = response.body().data
                    }
                }
            })

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        event?.let {
           if (it.code=="UPDATE_USER"){
               findUser()
           }
        }
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
    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
    }
}