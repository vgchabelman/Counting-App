package com.cornershop.countertest.presentation.main

import com.cornershop.countertest.domain.model.CounterListState

interface MainContract {
    interface MainView {
        fun render(state: CounterListState)
    }

    interface MainPresenter {
        fun bind(view: MainView)
        fun unbind()
        fun updateCounterList()
    }
}