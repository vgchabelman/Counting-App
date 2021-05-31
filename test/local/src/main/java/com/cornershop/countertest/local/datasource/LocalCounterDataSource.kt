package com.cornershop.countertest.local.datasource

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.local.dao.CounterDao
import com.cornershop.countertest.local.mapper.toCounter
import com.cornershop.countertest.local.mapper.toEntity
import java.util.UUID

class LocalCounterDataSource(
    private val counterDao: CounterDao
) : ICounterDataSource {
    override suspend fun getCounters(): List<Counter> {
        return counterDao.getAllCounters().map { it.toCounter() }
    }

    override suspend fun addCounter(counter: Counter): List<Counter> {
        if (counter.id.isBlank()) {
            counter.id = "temp-${UUID.randomUUID()}"
        }
        counterDao.create(counter.toEntity())
        return getCounters()
    }

    override suspend fun increment(counter: Counter): List<Counter> {
        var entity = counterDao.getCounter(counter.id)
        if (entity == null) {
            addCounter(counter)
            entity = counterDao.getCounter(counter.id)
        }
        entity!!.count++
        counterDao.update(entity)
        return getCounters()
    }

    override suspend fun decrement(counter: Counter): List<Counter> {
        var entity = counterDao.getCounter(counter.id)
        if (entity == null) {
            addCounter(counter)
            entity = counterDao.getCounter(counter.id)
        }
        entity!!.count--
        counterDao.update(entity)
        return getCounters()
    }

    override suspend fun removeCounter(counter: Counter): List<Counter> {
        counterDao.delete(counter.toEntity())
        return getCounters()
    }
}