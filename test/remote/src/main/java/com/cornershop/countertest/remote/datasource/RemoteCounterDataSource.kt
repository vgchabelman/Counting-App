package com.cornershop.countertest.remote.datasource

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.remote.dto.CounterRequestDto
import com.cornershop.countertest.remote.dto.CreateCounterRequestDto
import com.cornershop.countertest.remote.mapper.toCounter
import com.cornershop.countertest.remote.service.CounterServiceApi
import retrofit2.HttpException
import java.net.ConnectException

class RemoteCounterDataSource(private val api: CounterServiceApi) : ICounterDataSource {
    override suspend fun getCounters(): List<Counter> {
        try {
            return api.getCounters().map { it.toCounter() }
        } catch (e: HttpException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        }
    }

    override suspend fun addCounter(counter: Counter): List<Counter> {
        try {
            return api.addCounter(CreateCounterRequestDto(counter.title)).map { it.toCounter() }
        } catch (e: HttpException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        }
    }

    override suspend fun increment(counter: Counter): List<Counter> {
        try {
            return api.increment(CounterRequestDto(counter.id)).map { it.toCounter() }
        } catch (e: HttpException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        }
    }

    override suspend fun decrement(counter: Counter): List<Counter> {
        try {
            return api.decrement(CounterRequestDto(counter.id)).map { it.toCounter() }
        } catch (e: HttpException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        }
    }

    override suspend fun removeCounter(counter: Counter): List<Counter> {
        try {
            return api.removeCounter(CounterRequestDto(counter.id)).map { it.toCounter() }
        } catch (e: HttpException) {
            throw NoInternetException()
        } catch (e: ConnectException) {
            throw NoInternetException()
        }
    }
}