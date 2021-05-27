package com.cornershop.countertest.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.CounterSaveState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.launch

class CreateCounterViewModel(private val counterUseCase: CounterUseCase) : ViewModel() {
    val counterSaveState: LiveData<CounterSaveState>
        get() = _counterSaveState

    private val _counterSaveState: MutableLiveData<CounterSaveState> = MutableLiveData()

    fun saveCounter(title: String) {
        viewModelScope.launch {
            _counterSaveState.postValue(CounterSaveState.Loading)
            try {
                counterUseCase.saveCounter(title)
                _counterSaveState.postValue(CounterSaveState.SaveSuccess)
            } catch (e: NoInternetException) {
                _counterSaveState.postValue(CounterSaveState.Error(false))
            } catch (e: Exception) {
                _counterSaveState.postValue(CounterSaveState.Error())
            }
        }
    }
}