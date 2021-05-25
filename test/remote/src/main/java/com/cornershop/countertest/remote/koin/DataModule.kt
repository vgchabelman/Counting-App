package com.cornershop.countertest.remote.koin

import com.cornershop.countertest.remote.service.CounterServiceApi
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "localhost:3000/api/v1"

val dataModule = module {
    single { GsonBuilder().setLenient().create() }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory { provideCounterServiceApi(get()) }
}

fun provideCounterServiceApi(retrofit: Retrofit): CounterServiceApi =
    retrofit.create(CounterServiceApi::class.java)