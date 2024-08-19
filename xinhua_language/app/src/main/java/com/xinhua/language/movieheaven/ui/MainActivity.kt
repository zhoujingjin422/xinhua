package com.xinhua.language.movieheaven.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.xinhua.language.movieheaven.ads.AdUtils
import com.xinhua.language.R
import com.xinhua.language.databinding.ActivityMainBinding
import com.xinhua.language.movieheaven.BaseVMActivity
import com.xinhua.language.movieheaven.bean.DataBean
import com.xinhua.language.movieheaven.ext.getSpValue
import com.xinhua.language.movieheaven.ext.putSpValue
import com.xinhua.language.movieheaven.ext.versionName
import com.xinhua.language.movieheaven.utils.AdsMindDialog
import com.xinhua.language.movieheaven.utils.JsonCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseVMActivity() {


    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private  val viewModel by viewModel<MainViewModel>()
    private var url = "https://bywan.mmwcy.cn/"
    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        binding.apply {
            sfl.setOnClickListener {
                //跳转到搜索也没
                startActivity(
                    Intent(this@MainActivity,SearchListActivity::class.java)
                )
            }
            llBaidu.setOnClickListener {
                if(getSpValue("showYs",false)){
                    startActivity(Intent(this@MainActivity,WebPlayActivity::class.java)
                        .putExtra("title","柠檬影院").putExtra("url",
                            "$url?app=weike_android&uuid=${getAndroidID()}&version=${versionName}"
                        ))
                }else
                    startActivity(Intent(this@MainActivity,WebPlayActivity::class.java)
                        .putExtra("title","百度").putExtra("url","https://www.baidu.com/"))

            }
            llSina.setOnClickListener {
                startActivity(Intent(this@MainActivity,WebPlayActivity::class.java)
                    .putExtra("title","新浪").putExtra("url","https://www.sina.com.cn/"))
            }
            llSogou.setOnClickListener {
                startActivity(Intent(this@MainActivity,WebPlayActivity::class.java)
                    .putExtra("title","搜狗").putExtra("url","https://www.sogou.com/logo/monet/"))
            }
            llSohu.setOnClickListener {
                startActivity(Intent(this@MainActivity,WebPlayActivity::class.java).putExtra("title","搜狐").putExtra("url","https://www.sohu.com/"))
            }
            AdUtils.getInstance().bannerAd(this@MainActivity,banner)
        }
        if (getSpValue("url", "").isEmpty())
            putSpValue("url", url)
        else{
            url  = getSpValue("url", "")
        }
    }

    override fun onResume() {
        super.onResume()
        if(getSpValue("showYs",false)){
            binding.ivBaidu.setImageResource(R.mipmap.icon_yingshidaquan)
            binding.tvBaidu.text="影视大全"
        }else{
            binding.ivBaidu.setImageResource(R.mipmap.icon_baidu)
            binding.tvBaidu.text="百度"
        }
    }

    override fun initData() {
        OkGo.get<DataBean<String>>("https://uubabywang747.top/buried_point/adrsUni") // 请求方式和请求url
            .execute(object : JsonCallback<DataBean<String>>(DataBean::class.java) {
                override fun onSuccess(response: Response<DataBean<String>>) {
                    if (response.body().code==200){
                        url = response.body().data
                        putSpValue("url",url)
                    }
                }
            })
        OkGo.get<DataBean<List<String>>>("https://uubabywang747.top/buried_point/cmd") // 请求方式和请求url
            .execute(object : JsonCallback<DataBean<List<String>>>(DataBean::class.java) {
                override fun onSuccess(response: Response<DataBean<List<String>>>) {
                    if (response.body().code==200){
                        putSpValue("key",response.body().data.toString())
                    }
                }
            })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - startTime <= 5000) {
                finish()
            } else {
                startTime = System.currentTimeMillis()
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show()
            }
            false
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
    private var startTime = 0L
}