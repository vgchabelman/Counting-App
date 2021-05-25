package com.cornershop.counterstest

import com.cornershop.countertest.data.repository.CounterRepository
import com.cornershop.countertest.local.datasource.LocalCounterDataSource
import com.cornershop.countertest.remote.datasource.RemoteCounterDataSource
import org.koin.dsl.module

val dataModule = module {
    factory {
        CounterRepository(
            get(LocalCounterDataSource::class),
            get(RemoteCounterDataSource::class)
        )
    }
}