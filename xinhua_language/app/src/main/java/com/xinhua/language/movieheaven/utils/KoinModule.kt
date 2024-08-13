package com.xinhua.language.movieheaven.utils

import com.xinhua.language.movieheaven.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }
}
val appModule = listOf(viewModelModule)