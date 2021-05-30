package com.cornershop.countertest.data.repository

import com.cornershop.countertest.data.datasource.ICounterDataSource
import com.cornershop.countertest.data.datasource.MockDatasource
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CounterRepositoryTest {

    lateinit var localMockDataSource: MockDatasource
    lateinit var remoteMockDataSource: MockDatasource

    lateinit var repository: CounterRepository

    private val apiMockResponse = listOf(Counter("test", "test", 0))
    private val cacheMockResponse = listOf(Counter("test2", "test2", 1))

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        localMockDataSource = MockDatasource()
        remoteMockDataSource = MockDatasource()
        repository = CounterRepository(localMockDataSource, remoteMockDataSource)
    }

    @Test
    fun `it should return api results`() {
        runBlockingTest {
            localMockDataSource.mockList.addAll(cacheMockResponse)
            remoteMockDataSource.mockList.addAll(apiMockResponse)
            val result = repository.getAllCounters()
            Assert.assertEquals(apiMockResponse, result)
        }
    }

    @Test
    fun `it should return cache results`() {
        runBlockingTest {
            localMockDataSource.mockList.addAll(cacheMockResponse)
            val remoteMock: ICounterDataSource = Mockito.mock(ICounterDataSource::class.java)
            given(remoteMock.getCounters()).willAnswer { throw NoInternetException() }
            repository = CounterRepository(localMockDataSource, remoteMock)
            val result = repository.getAllCounters()
            Assert.assertEquals(cacheMockResponse, result)
        }
    }

    @Test
    fun `it should save new Counter to both remote and local sources`() {
        runBlockingTest {
            val testCounter = Counter("savingTest", "savingTest", 3)
            repository.saveCounter(testCounter)
            Assert.assertNotNull(localMockDataSource.mockList.contains(testCounter))
            Assert.assertNotNull(remoteMockDataSource.mockList.contains(testCounter))
        }
    }

    @Test
    fun `it should sync local source with remote source when saving new Counter`() {
        runBlockingTest {
            localMockDataSource.mockList.addAll(cacheMockResponse)
            remoteMockDataSource.mockList.addAll(apiMockResponse)
            val testCounter = Counter("savingTest", "savingTest", 3)
            repository.saveCounter(testCounter)
            Assert.assertEquals(remoteMockDataSource.mockList, localMockDataSource.mockList)
        }
    }
}