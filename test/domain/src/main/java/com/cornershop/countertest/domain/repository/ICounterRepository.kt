package com.cornershop.countertest.domain.repository

import com.cornershop.countertest.domain.model.Counter

interface ICounterRepository {
    suspend fun getAllCounters(): List<Counter>
    suspend fun saveCounter(counter: Counter)
    suspend fun incrementCounter(counter: Counter): List<Counter>
    suspend fun decrementCounter(counter: Counter): List<Counter>
}