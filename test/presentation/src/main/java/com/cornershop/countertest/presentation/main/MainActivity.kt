package com.cornershop.countertest.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main), MainContract.MainView {
    private val presenter: MainPresenter by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.bind(this)
    }

    override fun onResume() {
        super.onResume()

        presenter.updateCounterList()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbind()
    }

    override fun render(state: CounterListState) {
        when (state) {
            is CounterListState.LoadingState -> {
            }
            is CounterListState.DataState -> TODO()
            is CounterListState.ErrorState -> TODO()
        }
    }
}