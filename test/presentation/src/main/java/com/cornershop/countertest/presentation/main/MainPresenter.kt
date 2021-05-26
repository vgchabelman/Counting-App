package com.cornershop.countertest.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.usecase.CounterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainPresenter(
    private val useCase: CounterUseCase
): ViewModel(), MainContract.MainPresenter {
    private var view: MainContract.MainView? = null

    fun updateCounterList() {
        viewModelScope.launch(Dispatchers.IO) {
            view?.render(CounterListState.LoadingState)

            try {
                view?.render(CounterListState.DataState(useCase.getAllCounters()))
            } catch (e: Exception) {
                view?.render(CounterListState.ErrorState(e.message ?: "Unkown error"))
            }
        }
    }

    override fun bind(view: MainContract.MainView) {
        this.view = view
    }

    override fun unbind() {
        view = null
    }
}