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
        val oldList = remote.getCounters()
        val list = remote.addCounter(counter)
        list.filterNot { oldList.contains(it) }.forEach {
            local.addCounter(it)
        }
        remote.getCounters().filterNot { list.contains(it) }.forEach {
            remote.removeCounter(it)
        }
    }
}