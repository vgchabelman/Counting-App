package com.cornershop.countertest.domain.model

sealed class CounterSaveState {
    object Loading : CounterSaveState()
    object SaveSuccess : CounterSaveState()
    data class Error(val hasInternet: Boolean = true) : CounterSaveState()

}
