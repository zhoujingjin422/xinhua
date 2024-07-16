package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentSettingBinding
import com.xinhua.language.databinding.FragmentWriteBinding
import com.xinhua.language.wanbang.bean.UserBean
import com.xinhua.language.wanbang.ext.clickN
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import com.xinhua.language.wanbang.ext.dateTimeFormatter2
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }
    private var binding: FragmentSettingBinding? = null
    private val viewModel by sharedViewModel<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_setting,null)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            llNotLogin.clickN {
                startActivityForResult(Intent(requireActivity(),LoginActivity::class.java),10001)
            }
            flQu.clickN {
                KefuPop(requireContext()).showPopupWindow()
            }
            flFw.clickN {
                WebPlayActivity.startActivity(requireActivity(),"服务条款",Constant.URL_TERMS_OF_USE)
            }
            flYs.clickN {
                WebPlayActivity.startActivity(requireActivity(),"隐私协议",Constant.URL_PRIVACY_POLICY)
            }
            flAccount.clickN {
                startActivity(Intent(requireContext(),AccountSafeActivity::class.java))
            }
            ivNotVip.clickN {
                if (viewModel.isVip.value == false){
                    startActivity(Intent(requireActivity(),SubActivity::class.java))
                }
            }
            tvLogout.clickN {
                viewModel.isVip.postValue(false)
                viewModel.isLogin.postValue(false)
                viewModel.user.postValue(null)
                requireActivity().putSpValue("userPhone","")
            }
        }
        viewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (it){
                binding?.llNotLogin?.visibility =View.GONE
                binding?.clLogin?.visibility = View.VISIBLE
                binding?.flVip?.visibility = View.VISIBLE
                binding?.flAccount?.visibility = View.VISIBLE
            }else{
                binding?.llNotLogin?.visibility =View.VISIBLE
                binding?.clLogin?.visibility = View.GONE
                binding?.flVip?.visibility = View.GONE
                binding?.flAccount?.visibility = View.GONE
            }
        })
        viewModel.isVip.observe(viewLifecycleOwner, Observer {
            if (it){
                binding?.ivNotVip?.visibility = View.GONE
                binding?.rlVipNow?.visibility = View.VISIBLE
                binding?.tvEndDate?.text = "有效期至：${viewModel.user.value?.expiredTime}"
            }else{
                binding?.ivNotVip?.visibility = View.VISIBLE
                binding?.rlVipNow?.visibility = View.GONE
            }
        })
        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding?.tvPhone?.text = it?.phone?.substring(0,3)+"****"+it?.phone?.substring(it!!.phone!!.length-3,it!!.phone!!.length)
        })
        initData()
    }
    private fun initData(){
        if (viewModel.isLogin.value==true){
            binding?.llNotLogin?.visibility =View.GONE
            binding?.clLogin?.visibility = View.VISIBLE
            binding?.flVip?.visibility = View.VISIBLE
            binding?.flAccount?.visibility = View.VISIBLE
        }else{
            binding?.llNotLogin?.visibility =View.VISIBLE
            binding?.clLogin?.visibility = View.GONE
            binding?.flVip?.visibility = View.GONE
            binding?.flAccount?.visibility = View.GONE
        }
        if (viewModel.isVip.value==true){
            binding?.rlVipNow?.visibility = View.VISIBLE
            binding?.ivNotVip?.visibility = View.GONE
            binding?.tvEndDate?.text = "有效期至：${viewModel.user.value?.expiredTime}"
        }else{
            binding?.rlVipNow?.visibility = View.GONE
            binding?.flVip?.visibility = View.GONE
            binding?.ivNotVip?.visibility = View.VISIBLE
        }
        if (viewModel.user.value!=null){
            binding?.tvPhone?.text = viewModel.user.value?.phone?.substring(0,3)+"****"+viewModel.user.value?.phone?.substring(viewModel.user.value!!.phone!!.length-3,viewModel.user.value!!.phone!!.length)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==10001&&resultCode==10002){
           val userString = data?.getStringExtra("user")
            userString?.let {
                val user = Gson().fromJson(it,UserBean::class.java)
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