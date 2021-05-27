package com.cornershop.countertest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.model.CounterUpdateState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val useCase: CounterUseCase
) : ViewModel() {
    val counterListState: LiveData<CounterListState>
        get() = _counterListState
    private val _counterListState: MutableLiveData<CounterListState> = MutableLiveData()

    val counterUpdateState: LiveData<CounterUpdateState>
        get() = _counterUpdateState
    private val _counterUpdateState: MutableLiveData<CounterUpdateState> = MutableLiveData()

    fun updateCounterList() {
        viewModelScope.launch(Dispatchers.IO) {
            _counterListState.postValue(CounterListState.LoadingState)

            try {
                val list = useCase.getAllCounters()
                _counterListState.postValue(CounterListState.DataState(list))
            } catch (e: NoInternetException) {
                _counterListState.postValue(CounterListState.ErrorState(false))
            } catch (e: Exception) {
                _counterListState.postValue(CounterListState.ErrorState())
            }
        }
    }

    fun updateCounter(counter: Counter, increment: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = if (increment) {
                    useCase.incrementCounter(counter)
                } else {
                    useCase.decrementCounter(counter)
                }
                _counterUpdateState.postValue(CounterUpdateState.DataState(list))
            } catch (e: NoInternetException) {
                _counterUpdateState.postValue(CounterUpdateState.ErrorState(
                    false, counter, increment))
            } catch (e: Exception) {
                _counterUpdateState.postValue(CounterUpdateState.ErrorState(
                    counter = counter, increment = increment))
            }
        }
    }
}