package com.cornershop.countertest.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.model.CounterSelectedState
import com.cornershop.countertest.domain.model.CounterUpdateState
import com.cornershop.countertest.domain.model.CounterView
import com.cornershop.countertest.domain.usecase.CounterUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class MainViewModelTest {
    lateinit var mockUseCases: CounterUseCase
    lateinit var mainViewModel: MainViewModel

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockUseCases = Mockito.mock(CounterUseCase::class.java)
        mainViewModel = MainViewModel(mockUseCases)
    }

    @Test
    fun `if should be in CounterListState DataState`() {
        runBlockingTest {
            whenever(mockUseCases.getAllCounters()).thenReturn(listOf())
            mainViewModel.updateCounterList()
            assertTrue(mainViewModel.counterListState.value is CounterListState.DataState)
        }
    }

    @Test
    fun `if should be in CounterListState ErrorState with no Internet`() {
        runBlockingTest {
            given(mockUseCases.getAllCounters()).willAnswer { throw NoInternetException() }
            mainViewModel.updateCounterList()
            assertEquals(
                CounterListState.ErrorState(false),
                mainViewModel.counterListState.value
            )
        }
    }

    @Test
    fun `if should be in CounterListState ErrorState with Internet`() {
        runBlockingTest {
            given(mockUseCases.getAllCounters()).willAnswer { throw Exception() }
            mainViewModel.updateCounterList()
            assertEquals(
                CounterListState.ErrorState(true),
                mainViewModel.counterListState.value
            )
        }
    }

    @Test
    fun `it should call UseCases increment`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            mainViewModel.updateCounter(testCounter, true)
            verify(mockUseCases, times(1)).incrementCounter(testCounter)
        }
    }

    @Test
    fun `it should call UseCases decrement`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            mainViewModel.updateCounter(testCounter, false)
            verify(mockUseCases, times(1)).decrementCounter(testCounter)
        }
    }

    @Test
    fun `it should be in CounterStateList ErrorState with no Internet when incrementing`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            given(mockUseCases.incrementCounter(testCounter)).willAnswer { throw NoInternetException() }
            mainViewModel.updateCounter(testCounter, true)
            assertEquals(
                CounterUpdateState.ErrorState(false, testCounter, true),
                mainViewModel.counterUpdateState.value
            )
        }
    }

    @Test
    fun `it should be in CounterStateList ErrorState with Internet when incrementing`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            given(mockUseCases.incrementCounter(testCounter)).willAnswer { throw Exception() }
            mainViewModel.updateCounter(testCounter, true)
            assertEquals(
                CounterUpdateState.ErrorState(true, testCounter, true),
                mainViewModel.counterUpdateState.value
            )
        }
    }

    @Test
    fun `it should be in CounterStateList ErrorState with no Internet when decrementing`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            given(mockUseCases.decrementCounter(testCounter)).willAnswer { throw NoInternetException() }
            mainViewModel.updateCounter(testCounter, false)
            assertEquals(
                CounterUpdateState.ErrorState(false, testCounter, false),
                mainViewModel.counterUpdateState.value
            )
        }
    }

    @Test
    fun `it should be in CounterStateList ErrorState with Internet when decrementing`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            given(mockUseCases.decrementCounter(testCounter)).willAnswer { throw Exception() }
            mainViewModel.updateCounter(testCounter, false)
            assertEquals(
                CounterUpdateState.ErrorState(true, testCounter, false),
                mainViewModel.counterUpdateState.value
            )
        }
    }

    @Test
    fun `it should be in ChangeState after selecting`() {
        runBlockingTest {
            val testCounter = Counter("test", "test", 0)
            mainViewModel._counterListState.value =
                CounterListState.DataState(listOf(testCounter), false)
            whenever(mockUseCases.filterSearchResult(any(), any())).thenReturn(listOf(testCounter))
            mainViewModel.changeStateToSelect(testCounter)
            assertTrue(mainViewModel.selectedState.value is CounterSelectedState.ChangeState)
            val testCounterView = CounterView(testCounter, true)
            assertEquals(
                listOf(testCounterView),
                (mainViewModel.selectedState.value as CounterSelectedState.ChangeState).data
            )
        }
    }

    @Test
    fun `it should change selected status to selected in list`() {
        val testCounter = Counter("test", "test", 0)
        val testCounterView = CounterView(testCounter, false)
        mainViewModel._selectedState.value =
            CounterSelectedState.ChangeState(listOf(testCounterView))
        mainViewModel.selectCounter(testCounter)
        assertTrue(mainViewModel._selectedState.value is CounterSelectedState.DataState)
        val state = mainViewModel._selectedState.value as CounterSelectedState.DataState
        assertTrue(state.data[0].selected)
    }

    @Test
    fun `it should change selected status to unselected in list`() {
        val testCounter = Counter("test", "test", 0)
        val testCounterView = CounterView(testCounter, true)
        val testCounterView2 = CounterView(testCounter, true)
        mainViewModel._selectedState.value =
            CounterSelectedState.ChangeState(listOf(testCounterView, testCounterView2))
        mainViewModel.selectCounter(testCounter)
        assertTrue(mainViewModel._selectedState.value is CounterSelectedState.DataState)
        val state = mainViewModel._selectedState.value as CounterSelectedState.DataState
        assertFalse(state.data[0].selected)
    }

    @Test
    fun `it should be in CounterSelectedState SuccessState when there are no more selected`() {
        val testCounter = Counter("test", "test", 0)
        val testCounterView = CounterView(testCounter, true)
        mainViewModel._selectedState.value =
            CounterSelectedState.ChangeState(listOf(testCounterView))
        mainViewModel.selectCounter(testCounter)
        assertTrue(mainViewModel._selectedState.value is CounterSelectedState.SuccessState)
    }

    @Test
    fun `it should be in CounterSelectedState DeleteState when user call it`() {
        mainViewModel._selectedState.value = CounterSelectedState.ChangeState(listOf())
        mainViewModel.selectDelete()
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.DeleteState)
    }

    @Test
    fun `it should be in CounterSelectedState DataState when user cancels Delete`() {
        mainViewModel.cancelDelete(listOf())
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.DataState)
    }

    @Test
    fun `it should call useCases delete`() {
        runBlockingTest {
            mainViewModel.deleteCounters(listOf())
            verify(mockUseCases, times(1)).deleteCounters(listOf())
        }
    }

    @Test
    fun `it should be in CounterSelectedState DeleteErrorState when user has no Internet`() {
        runBlockingTest {
            given(mockUseCases.deleteCounters(any())).willAnswer { throw NoInternetException() }
            mainViewModel.deleteCounters(listOf())
            assertEquals(
                mainViewModel.selectedState.value,
                CounterSelectedState.DeleteErrorState(listOf(), false)
            )
        }
    }

    @Test
    fun `it should be in CounterSelectedState DeleteErrorState when user has Internet`() {
        runBlockingTest {
            given(mockUseCases.deleteCounters(any())).willAnswer { throw Exception() }
            mainViewModel.deleteCounters(listOf())
            assertEquals(
                mainViewModel.selectedState.value,
                CounterSelectedState.DeleteErrorState(listOf(), true)
            )
        }
    }

    @Test
    fun `it should be in CounterSelectedState ShareState when user call it`() {
        mainViewModel._selectedState.value = CounterSelectedState.ChangeState(listOf())
        mainViewModel.selectShare()
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.ShareState)
    }

    @Test
    fun `it should be in CounterSelectedState SuccessState when share is successful`() {
        mainViewModel.endShare(true, listOf())
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.SuccessState)
    }

    @Test
    fun `it should be in CounterSelectedState DataState when share is not successful`() {
        mainViewModel.endShare(false, listOf())
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.DataState)
    }

    @Test
    fun `it should be in CounterSelectedState SuccessState when user cancels`() {
        mainViewModel.cancelSelect()
        assertTrue(mainViewModel.selectedState.value is CounterSelectedState.SuccessState)
    }

    @Test
    fun `it should call use cases filter`() {
        mainViewModel.filterResults(listOf())
        verify(mockUseCases, times(1)).filterSearchResult(any(), any())
    }

    @Test
    fun `search query should be updated`() {
        mainViewModel.searchQuery = "test"
        mainViewModel.searchResults("changed")
        assertEquals("changed", mainViewModel.searchQuery)
    }

    @Test
    fun `search query should be empty after clear`() {
        mainViewModel.searchQuery = "test"
        mainViewModel.clearSearch()
        assertEquals("", mainViewModel.searchQuery)
    }
}