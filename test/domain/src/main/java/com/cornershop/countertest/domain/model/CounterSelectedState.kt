package com.cornershop.countertest.domain.model

sealed class CounterSelectedState {
    object SuccessState: CounterSelectedState()
    data class ChangeState(val data: List<CounterView>): CounterSelectedState()
    data class DataState(val data: List<CounterView>): CounterSelectedState()
    data class DeleteErrorState(val data: List<CounterView>, val hasInternet: Boolean = true): CounterSelectedState()
    data class DeleteState(val data: List<CounterView>): CounterSelectedState()
    data class ShareState(val data: List<CounterView>): CounterSelectedState()
}