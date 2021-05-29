package com.cornershop.countertest.domain.model

sealed class CounterSelectedState {
    object SuccessState: CounterSelectedState()
    data class ChangeState(val data: List<CounterView>): CounterSelectedState()
    data class DataState(val data: List<CounterView>): CounterSelectedState()
    data class ErrorState(val hasInternet: Boolean = true): CounterSelectedState()
}