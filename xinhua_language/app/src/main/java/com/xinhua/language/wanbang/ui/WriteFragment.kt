package com.xinhua.language.wanbang.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentWriteBinding

class WriteFragment:Fragment() {
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): WriteFragment {
            return WriteFragment()
        }
    }
    private var binding:FragmentWriteBinding? = null
    private var word = ""
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
            tvClick.setOnClickListener {
                //弹出输入框
                WordInputPop(requireContext(),tvWord.text.toString()) { word ->
                    if (word.isEmpty()) {
                        rlWord.visibility = View.GONE
                        tvClick.visibility = View.VISIBLE
                    } else {
                        rlWord.visibility = View.VISIBLE
                        tvClick.visibility = View.GONE
                    }
                    tvWordGray.text = word
                    tvWord.text = word
                    pv.clearCanvas()
                }.showPopupWindow()
            }
           ivChange.setOnClickListener {
                //弹出输入框
                WordInputPop(requireContext(),tvWord.text.toString()) { word ->
                    if (word.isEmpty()) {
                        rlWord.visibility = View.GONE
                        tvClick.visibility = View.VISIBLE
                    } else {
                        rlWord.visibility = View.VISIBLE
                        tvClick.visibility = View.GONE
                    }
                    tvWordGray.text = word
                    tvWord.text = word
                    pv.clearCanvas()
                }.showPopupWindow()
            }
            tvClear.setOnClickListener {
                binding?.pv?.clearCanvas()
            }
            tvWordShow.setOnClickListener {
                if(tvWordGray.visibility==View.VISIBLE){
                    tvWordGray.visibility = View.GONE
                    tv.text  ="白底"
                }else{
                    tvWordGray.visibility = View.VISIBLE
                    tv.text  ="灰字"
                }
            }
        }
    }
}