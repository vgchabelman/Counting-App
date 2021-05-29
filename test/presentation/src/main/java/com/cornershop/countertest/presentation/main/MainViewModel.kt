package com.cornershop.countertest.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.NoInternetException
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.model.CounterSelectedState
import com.cornershop.countertest.domain.model.CounterUpdateState
import com.cornershop.countertest.domain.model.CounterView
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ClassCastException

class MainViewModel(
    private val useCase: CounterUseCase
) : ViewModel() {
    val counterListState: LiveData<CounterListState>
        get() = _counterListState
    private val _counterListState: MutableLiveData<CounterListState> = MutableLiveData()

    val counterUpdateState: LiveData<CounterUpdateState>
        get() = _counterUpdateState
    private val _counterUpdateState: MutableLiveData<CounterUpdateState> = MutableLiveData()

    val selectedState: LiveData<CounterSelectedState>
        get() = _selectedState
    private val _selectedState: MutableLiveData<CounterSelectedState> = MutableLiveData()

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

                _counterListState.postValue(CounterListState.DataState(list))
            } catch (e: NoInternetException) {
                _counterUpdateState.postValue(CounterUpdateState.ErrorState(
                    false, counter, increment))
            } catch (e: Exception) {
                _counterUpdateState.postValue(CounterUpdateState.ErrorState(
                    counter = counter, increment = increment))
            }
        }
    }

    fun changeStateToSelect(counter: Counter) {
        val counterList = (counterListState.value as CounterListState.DataState).data.toCounterView()
        counterList.find { it.counter.id == counter.id }?.selected = true
        _selectedState.postValue(CounterSelectedState.ChangeState(counterList))
    }

    fun selectCounter(counter: Counter) {
        val selectedList = try {
            (selectedState.value as CounterSelectedState.DataState).data
        } catch (e: ClassCastException) {
            (selectedState.value as CounterSelectedState.ChangeState).data
        }.map { it.copy() }
        selectedList.find { it.counter.id ==  counter.id }?.let {
            it.selected = it.selected.not()
        }
        if (selectedList.none { it.selected }) {
            _selectedState.postValue(CounterSelectedState.SuccessState)
            return
        }
        _selectedState.postValue(CounterSelectedState.DataState(selectedList))
    }

    private fun List<Counter>.toCounterView(): List<CounterView> {
        return this.map {
            CounterView(it, false)
        }
    }
}