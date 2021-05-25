package com.cornershop.countertest.data.datasource

import com.cornershop.countertest.domain.model.Counter

interface ICounterDataSource {
    suspend fun getCounters(): List<Counter>

    suspend fun addCounter(counter: Counter): List<Counter>

    suspend fun increment(counter: Counter): List<Counter>

    suspend fun decrement(counter: Counter): List<Counter>

    suspend fun removeCounter(counter: Counter): List<Counter>
}