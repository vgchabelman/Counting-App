package com.cornershop.countertest.data.datasource

import com.cornershop.countertest.domain.model.Counter

class MockDatasource : ICounterDataSource {
    val mockList: MutableList<Counter> = mutableListOf()

    override suspend fun getCounters(): List<Counter> {
        return mockList
    }

    override suspend fun addCounter(counter: Counter): List<Counter> {
        mockList.add(counter)
        return mockList
    }

    override suspend fun increment(counter: Counter): List<Counter> {
        mockList.remove(counter)
        mockList.add(Counter(counter.id, counter.title, counter.count + 1))
        return mockList

    }

    override suspend fun decrement(counter: Counter): List<Counter> {
        mockList.remove(counter)
        mockList.add(Counter(counter.id, counter.title, counter.count - 1))
        return mockList
    }

    override suspend fun removeCounter(counter: Counter): List<Counter> {
        mockList.remove(counter)
        return mockList
    }
}