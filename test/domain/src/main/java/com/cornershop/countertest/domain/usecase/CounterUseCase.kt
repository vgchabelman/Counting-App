package com.cornershop.countertest.domain.usecase

import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.repository.ICounterRepository

class CounterUseCase(private val repository: ICounterRepository) {

    suspend fun getAllCounters() : List<Counter> = repository.getAllCounters()

    suspend fun saveCounter(title: String) {
        repository.saveCounter(Counter("", title, 0))
    }
}