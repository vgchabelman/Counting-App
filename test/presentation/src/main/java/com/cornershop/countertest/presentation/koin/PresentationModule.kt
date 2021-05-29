package com.cornershop.countertest.presentation.koin

import com.cornershop.countertest.presentation.create.CreateCounterViewModel
import com.cornershop.countertest.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        CreateCounterViewModel(get())
    }
}