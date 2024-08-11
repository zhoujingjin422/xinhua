package com.xinhua.language.wanbang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.xinhua.language.R
import com.xinhua.language.databinding.FragmentSettingBinding
import com.xinhua.language.wanbang.bean.UserBean
import com.xinhua.language.wanbang.ext.clickN
import com.xinhua.language.wanbang.ext.dateTimeFormatter1
import com.xinhua.language.wanbang.ext.putSpValue
import com.xinhua.language.wanbang.utils.Constant
import com.xinhua.language.wanbang.utils.StringUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.select.Elements
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
        val webView= WebView(requireContext())

        // 启用JavaScript
        webView.settings.javaScriptEnabled = true

        // 禁止外部浏览器打开新链接
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // 页面加载完成后调用JavaScript来提取数据
                view?.evaluateJavascript(
                    "(function() { return document.documentElement.outerHTML; })();"
                ) { html ->
                    // 在这里解析HTML数据
                    parseHtml(StringEscapeUtils.unescapeHtml4(html))
                }
            }
        }

        // 加载百度搜索页面
        webView.loadUrl("https://www.baidu.com/s?wd=明朝那些事儿")

        // 添加JavaScript接口
        webView.addJavascriptInterface(WebAppInterface(requireActivity()), "Android")
        initData()
    }
    private fun parseHtml(html: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val decodedHtml = html
                .replace("\\u003C", "<")
                .replace("\\\"", "\"")
//            var partiallyDecodedHtml = decodedHtml
//                .replace("\\&quot;", "&quot;")
//                .replace("\\&amp;", "&amp;")
//                .replace("\\&lt;", "&lt;")
//                .replace("\\&gt;", "&gt;")
//
//            // 第二步：将 &quot; 转换为 "
//            partiallyDecodedHtml = partiallyDecodedHtml
//                .replace("&quot;", "\"")
//                .replace("&amp;", "&")
//                .replace("&lt;", "<")
//                .replace("&gt;", ">")
//                .replace("\\u003E", ">")
//                .replace("\\u0026", "&")
//                .replace("\\u003D", "=")
//                .replace("\\u0027", "'")
//                .replace("\\u0022", "\"")
//            .replace("\\&quot;", "\"")
//            val transStr = StringUtils.transStr(decodedHtml)
            val connect = Jsoup.parse(decodedHtml)
//            val connect = Jsoup.connect("https://www.baidu.com/s?wd=IOS开发")
////                .cookies(cookies)
////                .data("query", "Java")
//                .userAgent("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36")
////                .userAgent("Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36")
////                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
//                .get()
            val results: Elements = connect.getElementsByClass("c-result result")
            val resultsContent: Elements = connect.getElementsByClass("c-result-content")
//            val results: Elements = connect.getElementsByClass("rw-list-new rw-list-new2")
//            val results: Elements = connect.select("a[href]")
//            val aResults: Elements = connect.select("a[href]")
//            val titleResults: Elements = connect.getElementsByClass("_no-spacing_1bhnz_26 cu-line-clamp1")
            results.forEachIndexed { index, element ->
                val linkElement = resultsContent[index]
                val bikeElement = element.getElementsByClass("_no-spacing_eq0t5_4").first()
                val link = linkElement.select("article").first().attr("rl-link-href")
                val tittleElement =  element.getElementsByClass("_no-spacing_1bhnz_26 cu-line-clamp1").first()
                val descElement =  element.getElementsByClass(" c-color summary-gap_3Jb4I").first()
                if (tittleElement!=null&&link.isNotEmpty()){
                    val title = tittleElement.text()
                    if (descElement!=null){
                        val desc = descElement.text()
                        println("element$index:Title: $title")
                        println("element$index:Desc: $desc")
                        println("element$index:link: $link")
                        println("element----------------------------------")
                    }
                    if (bikeElement!=null){
                        val desc = bikeElement.text()
                        println("element$index:Title: $title")
                        println("element$index:Desc: $desc")
                        println("element$index:link: $link")
                        println("element----------------------------------")
                    }
                }

            }
//            descResults.forEachIndexed { index, element ->
//                val desc = element.text()
//               val title = titleResults[index].text()?:""
//                println("Title: $title,desc：${desc}")
//            }
//            descResults.forEach {
//                val title = it.text()
//                println("desc: $title")
//            }
//            val results: Elements = connect.getElementsByTag("a")
            //        val results: Elements = doc.select("a[href]") // 选择<h3>标签中的<a>链接
//            for (result in results) {
////                val results = result.select("a[href]").first()
//                val resultTittle =  result.getElementsByClass("_no-spacing_1bhnz_26 cu-line-clamp1")
//                val title = result.text()
//                val link = result.attr("href")
//                println("Title: $title, Link: $link")
////                val cResult = result.getElementsByClass("c-result result")
////                if (cResult.size>0){
////
////                }
//
//                // 处理每个搜索结果，显示在RecyclerView中或其他UI组件中
//            }
        }
//        val results: Elements = doc.select("a[href]") // 选择<h3>标签中的<a>链接
//        val results: Elements = doc.getElementsByClass("hint-rcmd-item-container") // 选择<h3>标签中的<a>链接
//        val results: Elements = doc.getElementsByTag("a")
////
//        for (result in results) {
//            val title = result.text()
//            val link = result.attr("href")
//            // 处理每个搜索结果，显示在RecyclerView中或其他UI组件中
//            println("Title: $title, Link: $link")
//        }
    }
    class WebAppInterface(private val context: Context) {

        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
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