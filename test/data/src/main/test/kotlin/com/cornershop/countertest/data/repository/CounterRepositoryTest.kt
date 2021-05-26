package com.cornershop.countertest.data.repository

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.domain.model.Counter
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

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        MockitoAnnotations.initMocks(this)
        repository = CounterRepository(localMockDataSource, remoteMockDataSource)
    }

    @Test
    fun `it should return api results`() {
        runBlockingTest {
            val mockList = listOf(Counter("test", "test", 0))
            whenever(remoteMockDataSource.getCounters()).thenAnswer { mockList }
            val result = repository.getAllCounters()
            Assert.assertEquals(mockList, result)
        }
    }
}