package com.cornershop.countertest.domain.repository

import com.cornershop.countertest.domain.model.Counter

class MockCounterRepository : ICounterRepository {
    val mockList: MutableList<Counter> = mutableListOf()

    override suspend fun getAllCounters(): List<Counter> = mockList

    override suspend fun saveCounter(counter: Counter) {
        mockList.add(counter)
    }

    override suspend fun incrementCounter(counter: Counter): List<Counter> {
        mockList.remove(counter)
        mockList.add(Counter(counter.id, counter.title, counter.count + 1))
        return mockList
    }

    override suspend fun decrementCounter(counter: Counter): List<Counter> {
        mockList.remove(counter)
        mockList.add(Counter(counter.id, counter.title, counter.count - 1))
        return mockList
    }

    override suspend fun deleteCounter(counter: Counter) {
        mockList.remove(counter)
    }
}