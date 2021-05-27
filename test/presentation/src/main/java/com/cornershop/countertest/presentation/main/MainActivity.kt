package com.cornershop.countertest.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.presentation.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val presenter: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeState()
    }

    override fun onResume() {
        super.onResume()

        presenter.updateCounterList()
    }

    private fun observeState() {
        presenter.counterListState.observe(this) { state ->
            when (state) {
                is CounterListState.LoadingState -> {
                    handleLoadingState()
                }
                is CounterListState.DataState -> {
                    handleDataState(state)
                }
                is CounterListState.ErrorState -> {
                    handleErrorState(state)
                }
            }
        }
    }

    private fun handleLoadingState() {
        binding.mainLoading.loading.show()
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = false
        binding.mainError.root.isVisible = false
    }

    private fun handleDataState(state: CounterListState.DataState) {
        binding.mainLoading.loading.hide()
        binding.mainError.root.isVisible = false

        if (state.data.isEmpty()) {
            binding.mainNoCounters.root.isVisible = true
            binding.mainCounter.mainCounterSwipe.isVisible = false
            return
        }
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = true
    }

    private fun handleErrorState(state: CounterListState.ErrorState) {
        binding.mainLoading.loading.hide()
        binding.mainError.root.isVisible = true
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = false
        Log.e("Error", state.data)
    }
}