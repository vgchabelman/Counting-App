package com.cornershop.countertest.data.repository

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.model.Counter

class CounterRepository(
    private val local: ICounterDataSource,
    private val remote: ICounterDataSource
) {

    suspend fun getAllCounters(): List<Counter> {
        return try {
            remote.getCounters()
        } catch (e: NoInternetException) {
            local.getCounters()
        }
    }
}