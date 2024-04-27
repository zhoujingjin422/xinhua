package com.xinhua.language.wanbang.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    var isLogin = MutableLiveData(false)
    var isVip = MutableLiveData(false)
}