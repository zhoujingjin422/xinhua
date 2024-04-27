package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentSettingBinding
import com.xinhua.language.databinding.FragmentWriteBinding
import com.xinhua.language.wanbang.utils.Constant

class SettingFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): SettingFragment {
            return SettingFragment()
        }
    }
    private var binding: FragmentSettingBinding? = null
    private lateinit var viewModel: MainViewModel
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
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding?.apply {
            llNotLogin.setOnClickListener {
                startActivity(Intent(requireActivity(),LoginActivity::class.java))
                viewModel.isLogin.postValue(true)
            }
            flQu.setOnClickListener {
                KefuPop(requireContext()).showPopupWindow()
            }
            flFw.setOnClickListener {
                WebPlayActivity.startActivity(requireActivity(),"服务条款",Constant.URL_TERMS_OF_USE)
            }
            flYs.setOnClickListener {
                WebPlayActivity.startActivity(requireActivity(),"隐私协议",Constant.URL_PRIVACY_POLICY)
            }
            ivVip.setOnClickListener {
                if (viewModel.isVip.value == false){
                    startActivity(Intent(requireActivity(),SubActivity::class.java))
                    viewModel.isVip.postValue(true)
                }
            }
        }
        viewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (it){
                binding?.llNotLogin?.visibility =View.GONE
                binding?.clLogin?.visibility = View.VISIBLE
                binding?.ivVip?.visibility = View.VISIBLE
            }else{
                binding?.llNotLogin?.visibility =View.VISIBLE
                binding?.clLogin?.visibility = View.GONE
                binding?.ivVip?.visibility = View.GONE
            }
        })
        viewModel.isVip.observe(viewLifecycleOwner, Observer {
            if (it){
                binding?.ivVip?.setImageResource(R.mipmap.icon_vip)
            }else{
                binding?.ivVip?.setImageResource(R.mipmap.icon_not_vip)
            }
        })
    }
}