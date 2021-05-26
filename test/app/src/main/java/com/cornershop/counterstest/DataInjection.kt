package com.cornershop.counterstest

import com.cornershop.countertest.data.repository.CounterRepository
import com.cornershop.countertest.domain.repository.ICounterRepository
import com.cornershop.countertest.domain.usecase.CounterUseCase
import com.cornershop.countertest.local.datasource.LocalCounterDataSource
import com.cornershop.countertest.presentation.main.MainPresenter
import com.cornershop.countertest.remote.datasource.RemoteCounterDataSource
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    factory {
        CounterRepository(
            get(LocalCounterDataSource::class),
            get(RemoteCounterDataSource::class)
        )
    } as ICounterRepository
}

val domainModule = module {
    factory {
        CounterUseCase(get())
    }
}