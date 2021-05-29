package com.cornershop.countertest.domain.usecase

import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.repository.ICounterRepository

class CounterUseCase(private val repository: ICounterRepository) {

    suspend fun getAllCounters() : List<Counter> = repository.getAllCounters()

    suspend fun saveCounter(title: String) {
        repository.saveCounter(Counter("", title, 0))
    }

    suspend fun incrementCounter(counter: Counter): List<Counter> {
        return repository.incrementCounter(counter)
    }

    suspend fun decrementCounter(counter: Counter): List<Counter> {
        return repository.decrementCounter(counter)
    }

    suspend fun deleteCounters(counterList: List<Counter>) {
        counterList.forEach { counter ->
            repository.deleteCounter(counter)
        }
    }
}