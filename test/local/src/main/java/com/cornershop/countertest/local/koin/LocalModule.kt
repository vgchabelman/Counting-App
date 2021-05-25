package com.cornershop.countertest.local.koin

import androidx.room.Room
import com.cornershop.countertest.local.dao.CounterDao
import com.cornershop.countertest.local.datasource.LocalCounterDataSource
import com.cornershop.countertest.local.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "counter_database"
        ).build()
    }

    factory { provideCounterDao(get()) }
    factory { LocalCounterDataSource(get()) }
}

fun provideCounterDao(appDatabase: AppDatabase): CounterDao = appDatabase.counterDao()