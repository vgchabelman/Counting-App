package com.cornershop.counterstest

import android.app.Application
import com.cornershop.countertest.local.koin.localModule
import com.cornershop.countertest.remote.koin.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CountingApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CountingApplication)

            module {
                listOf(
                    dataModule,
                    localModule,
                    remoteModule
                )
            }
        }
    }
}