package com.cornershop.countertest.presentation.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.CounterSaveState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.lang.Exception

@ExperimentalCoroutinesApi
class CreateCounterViewModelTest {
    lateinit var mockUseCases: CounterUseCase
    lateinit var createCounterViewModel: CreateCounterViewModel

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockUseCases = Mockito.mock(CounterUseCase::class.java)
        createCounterViewModel = CreateCounterViewModel(mockUseCases)
    }

    @Test
    fun `it should be in the SuccessState when successful`() {
        runBlockingTest {
            createCounterViewModel.saveCounter("")
            assertEquals(
                CounterSaveState.SaveSuccess,
                createCounterViewModel.counterSaveState.value
            )
        }
    }

    @Test
    fun `it should be in the ErrorState when there is no Internet`() {
        runBlockingTest {
            given(mockUseCases.saveCounter("")).willAnswer { throw NoInternetException() }
            createCounterViewModel.saveCounter("")
            assertEquals(
                CounterSaveState.Error(false),
                createCounterViewModel.counterSaveState.value
            )
        }
    }

    @Test
    fun `it should be in the ErrorState when saving fails with Internet`() {
        runBlockingTest {
            given(mockUseCases.saveCounter("")).willAnswer { throw Exception() }
            createCounterViewModel.saveCounter("")
            assertEquals(
                CounterSaveState.Error(true),
                createCounterViewModel.counterSaveState.value
            )
        }
    }
}