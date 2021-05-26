package com.cornershop.countertest.data.repository

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.model.Counter
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CounterRepositoryTest {

    @Mock
    lateinit var localMockDataSource: ICounterDataSource

    @Mock
    lateinit var remoteMockDataSource: ICounterDataSource
    lateinit var repository: CounterRepository

    private val apiMockResponse = listOf(Counter("test", "test", 0))
    private val cacheMockResponse = listOf(Counter("test2", "test2", 1))

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        MockitoAnnotations.initMocks(this)
        repository = CounterRepository(localMockDataSource, remoteMockDataSource)
    }

    @Test
    fun `it should return api results`() {
        runBlockingTest {
            whenever(remoteMockDataSource.getCounters()).thenAnswer { apiMockResponse }
            whenever(localMockDataSource.getCounters()).thenAnswer { cacheMockResponse }
            val result = repository.getAllCounters()
            Assert.assertEquals(apiMockResponse, result)
        }
    }

    @Test
    fun `it should return cache results`() {
        runBlockingTest {
            whenever(localMockDataSource.getCounters()).thenAnswer { cacheMockResponse }
            given(remoteMockDataSource.getCounters()).willAnswer { throw NoInternetException() }
            val result = repository.getAllCounters()
            Assert.assertEquals(cacheMockResponse, result)
        }
    }
}