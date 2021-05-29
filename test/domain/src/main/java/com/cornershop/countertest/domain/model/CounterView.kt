package com.cornershop.countertest.domain.model

data class CounterView(
    val counter: Counter,
    var selected: Boolean = false
)