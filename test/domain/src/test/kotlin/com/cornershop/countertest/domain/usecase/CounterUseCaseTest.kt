package com.cornershop.countertest.domain.usecase

import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.repository.MockCounterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    @Test
    fun `it should return repository Counter list`() {
        runBlockingTest {
            val list = counterUseCase.getAllCounters()
            assertEquals(mockCounterRepository.mockList, list)
        }
    }

    @Test
    fun `it should call repository increment`() {
        runBlockingTest {
            val counter = Counter("test", "test", 0)
            val list = counterUseCase.incrementCounter(counter)
            assertEquals(mockCounterRepository.mockList, list)
            assertEquals(1, list.first { it.id == counter.id }.count)
        }
    }

    @Test
    fun `it should call repository decrement`() {
        runBlockingTest {
            val counter = Counter("test", "test", 10)
            val list = counterUseCase.decrementCounter(counter)
            assertEquals(mockCounterRepository.mockList, list)
            assertEquals(9, list.first { it.id == counter.id }.count)
        }
    }

    @Test
    fun `it should call repository delete`() {
        runBlockingTest {
            val counterList = listOf(
                Counter("test", "test", 0),
                Counter("test2", "test2", 0)
            )
            mockCounterRepository.mockList.addAll(counterList)
            counterUseCase.deleteCounters(counterList)
            assertTrue(mockCounterRepository.mockList.isEmpty())
        }
    }

    @Test
    fun `it should filter list by query regardless of case`() {
        runBlockingTest {
            val counterList = listOf(
                Counter("test", "test", 0),
                Counter("test2", "TEST", 0)
            )
            val result = counterUseCase.filterSearchResult(counterList, "test")
            assertEquals(counterList, result)
        }
    }

    @Test
    fun `it should filter out of list by query`() {
        runBlockingTest {
            val counterList = listOf(
                Counter("test", "test", 0),
                Counter("test2", "remove me", 0)
            )
            val result = counterUseCase.filterSearchResult(counterList, "test")
            assertEquals(listOf(Counter("test", "test", 0)), result)
        }
    }
}