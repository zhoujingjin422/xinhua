package com.xinhua.language.wanbang.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentWriteBinding
import com.xinhua.language.wanbang.ext.clickN

class WriteFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): WriteFragment {
            return WriteFragment()
        }
    }
    private var binding:FragmentWriteBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_write,null)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            rv.layoutManager = LinearLayoutManager(context)
        }

    }
}