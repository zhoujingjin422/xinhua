package com.xinhua.language.movieheaven.bean

data  class DataBean<T>(
    val code: Int,
    val data: T,
    val msg: String
)