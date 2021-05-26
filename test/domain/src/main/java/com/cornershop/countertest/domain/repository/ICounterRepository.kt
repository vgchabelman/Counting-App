package com.cornershop.countertest.domain.repository

import com.cornershop.countertest.domain.model.Counter

interface ICounterRepository {
    suspend fun getAllCounters(): List<Counter>
}