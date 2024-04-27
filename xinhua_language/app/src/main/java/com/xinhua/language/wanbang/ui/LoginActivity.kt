package com.xinhua.language.wanbang.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityLoginBinding
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.utils.Constant
import com.xinhua.language.wanbang.utils.MyCountDownTimer

class LoginActivity:BaseVMActivity() {
    private val binding by binding<ActivityLoginBinding>(R.layout.activity_login)
    private var str1 =
        "我已阅读并同意"
    private var str2 = "《服务协议》"
    private var str3 = "和"
    private var str4 = "《用户隐私协议》"
      override fun initView() {
        binding.apply {
            ivClose.setOnClickListener {
                finish()
            }
            etPhone.addTextChangedListener { etp->
                if (etp?.length==11){
                    tvGetCode.setTextColor(resources.getColor(R.color.c_e32122))
                    tvGetCode.isClickable = true
                }else{
                    tvGetCode.isClickable = false
                    tvGetCode.setTextColor(resources.getColor(R.color.c_b0b0b0))
                }
                cb.setOnCheckedChangeListener { _, b ->
                    if (b){
                        tvLogin.setTextColor((resources.getColor(R.color.white)))
                    }else{
                        tvLogin.setTextColor((resources.getColor(R.color.c_b0b0b0)))
                    }
                }
                tvGetCode.setOnClickListener {
                    MyCountDownTimer(60000L,1000L,{time->
                        tvGetCode.text = "${time}s"
                    },{
                        tvGetCode.text = "获取短信验证码"
                        tvGetCode.isClickable = true
                    }).start()
                    tvGetCode.isClickable = false
                }
                val spannableString = SpannableString(str1 + str2 + str3 + str4)
                spannableString.setSpan(
                    resources.getColor(R.color.c_0e69c7),
                    str1.length,
                    str1.length + str2.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        WebPlayActivity.startActivity(this@LoginActivity,"服务协议",Constant.URL_TERMS_OF_USE)
                    }
                }, str1.length, str1.length + str2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(
                    resources.getColor(R.color.c_0e69c7),
                    str1.length + str2.length + str3.length,
                    str1.length + str2.length + str3.length + str4.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(p0: View) {
                            WebPlayActivity.startActivity(this@LoginActivity,"用户隐私协议",Constant.URL_PRIVACY_POLICY)
                        }
                    },
                    str1.length + str2.length + str3.length,
                    str1.length + str2.length + str3.length + str4.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvCb.movementMethod = LinkMovementMethod.getInstance()
                tvCb.text = spannableString
            }
        }

    }

    override fun initData() {
    }
}