package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentHomeBinding
import com.xinhua.language.wanbang.bean.UserBean
import com.xinhua.language.wanbang.ext.clickN
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Calendar

class HomeFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }
private var  binding:FragmentHomeBinding? = null
    private val viewModel by sharedViewModel<MainViewModel>()
    private val resImage = arrayOf(R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4
        ,R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4,
        R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4
        ,R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4,
        R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4,
        R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4,
        R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4,
        R.mipmap.banner_1,R.mipmap.banner_2,R.mipmap.banner_3,R.mipmap.banner_4)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home,null)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            flSearch.clickN {
               if (viewModel.isLogin.value==true){
                    if (viewModel.isVip.value==true){
                        WebPlayPianoActivity.startActivity(requireActivity())
                    }else{
                        startActivity(Intent(requireActivity(),SubActivity::class.java))
                    }
                }else{
                    startActivityForResult(Intent(requireActivity(),LoginActivity::class.java),30001)
                }
            }
            try {
                ivBanner.setImageResource(resImage[Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1])
            }catch (e:Exception){

            }
            ivBanner.clickN {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://treetop.cpdtlp.com.cn/misc/download-app")).apply {
                    // 在新的任务栈中启动活动
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==30001&&resultCode==10002){
            val userString = data?.getStringExtra("user")
            userString?.let {
                val user = Gson().fromJson(it, UserBean::class.java)
                viewModel.isLogin.postValue(true)
                viewModel.user.postValue(user)
                if (!user.expiredTime.isNullOrEmpty()){
                    if(dateTimeFormatter1.parse(user.expiredTime).time>System.currentTimeMillis()){
                        viewModel.isVip.postValue(true)
                    }else{
                        viewModel.isVip.postValue(false)
                    }
                }else{
                    viewModel.isVip.postValue(false)
                }
            }
        }
    }
}