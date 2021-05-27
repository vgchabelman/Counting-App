package com.cornershop.countertest.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPresenter(
    private val useCase: CounterUseCase
) : ViewModel(), MainContract.MainPresenter {
    private var view: MainContract.MainView? = null

    override fun bind(view: MainContract.MainView) {
        this.view = view
    }

    override fun unbind() {
        view = null
    }

    override fun updateCounterList() {
        viewModelScope.launch(Dispatchers.IO) {
            view?.render(CounterListState.LoadingState)

            try {
                view?.render(CounterListState.DataState(useCase.getAllCounters()))
            } catch (e: Exception) {
                view?.render(CounterListState.ErrorState(e.message ?: "Unknown error"))
            }
        }
    }
}