package com.cornershop.countertest.domain.usecase

import com.cornershop.countertest.domain.repository.MockCounterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CounterUseCaseTest {
    lateinit var mockCounterRepository: MockCounterRepository
    lateinit var counterUseCase: CounterUseCase

    @Before
    fun setUp() {
        mockCounterRepository = MockCounterRepository()
        counterUseCase = CounterUseCase(mockCounterRepository)
    }

    @Test
    fun `it should save new Counter to repository`() {
        runBlockingTest {
            counterUseCase.saveCounter("test")
            assertNotNull(mockCounterRepository.mockList.find { it.title == "test" })
        }
    }
}