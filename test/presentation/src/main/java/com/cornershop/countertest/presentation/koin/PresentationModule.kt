package com.cornershop.countertest.presentation.koin

import com.cornershop.countertest.presentation.main.MainPresenter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MainPresenter(get())
    }
}