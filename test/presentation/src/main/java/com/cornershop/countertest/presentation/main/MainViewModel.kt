package com.cornershop.countertest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val useCase: CounterUseCase
) : ViewModel() {
    val counterListState: LiveData<CounterListState>
        get() = _counterListState

    private val _counterListState: MutableLiveData<CounterListState> = MutableLiveData()

    fun updateCounterList() {
        viewModelScope.launch(Dispatchers.IO) {
            _counterListState.postValue(CounterListState.LoadingState)

            try {
                val list = useCase.getAllCounters()
                _counterListState.postValue(CounterListState.DataState(list))
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                _counterListState.postValue(CounterListState.ErrorState(message))
            }
        }
    }
}