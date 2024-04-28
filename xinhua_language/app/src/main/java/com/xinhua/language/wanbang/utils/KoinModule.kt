package com.xinhua.language.wanbang.utils

import com.xinhua.language.wanbang.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.Collections.list

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }
}
val appModule = listOf(viewModelModule)