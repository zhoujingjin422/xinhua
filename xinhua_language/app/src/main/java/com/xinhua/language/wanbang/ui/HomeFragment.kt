package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }
private var  binding:FragmentHomeBinding? = null
    private val viewModel by sharedViewModel<MainViewModel>()

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
            flSearch.setOnClickListener {
                if (viewModel.isLogin.value==true){
                    if (viewModel.isVip.value==true){
                        WebPlayPianoActivity.startActivity(requireActivity())
                    }else{
                        startActivity(Intent(requireActivity(),SubActivity::class.java))
                    }
                }else{
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                }

            }
        }
    }
}