package com.cornershop.countertest.remote.koin

import com.cornershop.countertest.remote.BuildConfig
import com.cornershop.countertest.remote.datasource.RemoteCounterDataSource
import com.cornershop.countertest.remote.service.CounterServiceApi
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://10.0.2.2:3000/api/v1/"

val remoteModule = module {
    single { GsonBuilder().setLenient().create() }

    single {
        val client = OkHttpClient.Builder()

        val logger = HttpLoggingInterceptor()
        logger.level = BuildConfig.LOG_LEVEL

        client.addInterceptor(logger)
        client.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }.readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory { provideCounterServiceApi(get()) }
    factory { RemoteCounterDataSource(get()) }
}

fun provideCounterServiceApi(retrofit: Retrofit): CounterServiceApi =
    retrofit.create(CounterServiceApi::class.java)