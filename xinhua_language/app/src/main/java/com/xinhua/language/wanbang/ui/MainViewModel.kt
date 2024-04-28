package com.xinhua.language.wanbang.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xinhua.language.wanbang.bean.UserBean

class MainViewModel:ViewModel() {
    var isLogin = MutableLiveData(false)
    var isVip = MutableLiveData(false)
    var user = MutableLiveData<UserBean?>()
}