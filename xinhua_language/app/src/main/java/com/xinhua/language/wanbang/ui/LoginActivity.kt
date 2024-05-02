package com.xinhua.language.wanbang.ui

import android.content.Intent
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityLoginBinding
import com.xinhua.language.wanbang.BaseVMActivity
import com.xinhua.language.wanbang.bean.LoginBean
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import com.xinhua.language.wanbang.utils.JsonCallback
import com.xinhua.language.wanbang.utils.MyCountDownTimer

class LoginActivity:BaseVMActivity() {
    private val binding by binding<ActivityLoginBinding>(R.layout.activity_login)
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
            }
                cb.setOnCheckedChangeListener { _, b ->
                    if (b){
                        tvLogin.setTextColor((resources.getColor(R.color.white)))
                    }else{
                        tvLogin.setTextColor((resources.getColor(R.color.c_b0b0b0)))
                    }
                }
                tvLogin.setOnClickListener {
                    if (cb.isChecked)
                    login()
                    else{
                        Toast.makeText(this@LoginActivity,"请阅读并同意《用户服务协议》和《用户隐私协议》",Toast.LENGTH_SHORT).show()
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
                    getCode()
                }
                tvFwxy.setOnClickListener {
                    WebPlayActivity.startActivity(
                        this@LoginActivity,
                        "服务协议",
                        Constant.URL_TERMS_OF_USE
                    )
                }
                tvYsxy.setOnClickListener {
                    WebPlayActivity.startActivity(
                        this@LoginActivity,
                        "用户隐私协议",
                        Constant.URL_PRIVACY_POLICY
                    )
                }
            }
    }

    /**
     * 获取验证码
     */
    private fun getCode(){
        val map = mutableMapOf<String,String>()
        map["phone"] = binding.etPhone.text.toString()
        map["type"] = "xinhua_dict&prod"
        OkGo.post<String>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/user/sendVerifyCode") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                   if (response.code()==200){
                        Toast.makeText(this@LoginActivity,"验证码已发送",Toast.LENGTH_SHORT).show()
                   }
                }
            })
    }

    /**
     * 登录注册
     */
    private fun login(){
        if (binding.etPhone.text.toString().isEmpty()||binding.etCode.text.toString().isEmpty()){
            Toast.makeText(this,"手机号或验证码不能为空",Toast.LENGTH_SHORT).show()
            return
        }
        val map = mutableMapOf<String,String>()

        map["phone"] = binding.etPhone.text.toString()
        map["code"] = binding.etCode.text.toString()
        OkGo.post<LoginBean>("https://cndicttest.cpdtlp.com.cn/dict_serve/api/user/registerUser") // 请求方式和请求url
            .upJson(Gson().toJson(map))
            .execute(object : JsonCallback<LoginBean>(LoginBean::class.java) {
                override fun onSuccess(response: Response<LoginBean>) {
                    if (response.code()==200){
                        //登录成功
                        putSpValue("userPhone",response.body().data.user.phone)
                        putSpValue("token",response.body().data.token)
                        Toast.makeText(this@LoginActivity,"登录成功",Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        intent.putExtra("user", Gson().toJson(response.body().data.user))
                        setResult(10002, intent)
                       finish()
                    }else{
                        Toast.makeText(this@LoginActivity,response.message(),Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }
    override fun initData() {
    }
}