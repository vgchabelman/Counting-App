package com.cornershop.countertest.domain.model

sealed class CounterListState {
    object LoadingState : CounterListState()
    data class DataState(val data: List<Counter>, val isSearching: Boolean): CounterListState()
    data class ErrorState(val hasInternet: Boolean = true): CounterListState()
}