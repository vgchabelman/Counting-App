package com.cornershop.countertest.domain.model

sealed class CounterUpdateState {
    data class ErrorState(
        val hasInternet: Boolean = true,
        val counter: Counter,
        val increment: Boolean
    ): CounterUpdateState()
}