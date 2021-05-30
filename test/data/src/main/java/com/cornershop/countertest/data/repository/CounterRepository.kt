package com.cornershop.countertest.data.repository

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.repository.ICounterRepository

class CounterRepository(
    private val local: ICounterDataSource,
    private val remote: ICounterDataSource
) : ICounterRepository {

    override suspend fun getAllCounters(): List<Counter> {
        return try {
            remote.getCounters()
        } catch (e: NoInternetException) {
            local.getCounters()
        }
    }

    override suspend fun saveCounter(counter: Counter) {
        val localList = local.getCounters()
        val remoteList = remote.addCounter(counter)
        remoteList.filterNot { localList.contains(it) }.forEach {
            local.addCounter(it)
        }
        localList.filterNot { remoteList.contains(it) }.forEach {
            local.removeCounter(it)
        }
    }

    override suspend fun incrementCounter(counter: Counter): List<Counter> {
        val list = remote.increment(counter)
        local.increment(counter)
        return list
    }

    override suspend fun decrementCounter(counter: Counter): List<Counter> {
        val list = remote.decrement(counter)
        local.decrement(counter)
        return list
    }

    override suspend fun deleteCounter(counter: Counter) {
        remote.removeCounter(counter)
        local.removeCounter(counter)
    }
}