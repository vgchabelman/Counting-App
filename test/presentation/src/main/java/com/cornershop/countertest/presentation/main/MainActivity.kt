package com.cornershop.countertest.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.model.CounterUpdateState
import com.cornershop.countertest.presentation.R
import com.cornershop.countertest.presentation.create.CreateCounterActivity
import com.cornershop.countertest.presentation.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val adapterCounter = setupAdapter()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainCounter.mainCounterList.adapter = adapterCounter

        binding.addCounterButton.setOnClickListener {
            startActivity(Intent(this, CreateCounterActivity::class.java))
        }
        observeState()
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateCounterList()
    }

    private fun observeState() {
        viewModel.counterListState.observe(this) { state ->
            when (state) {
                is CounterListState.LoadingState -> handleLoadingState()
                is CounterListState.DataState -> handleDataState(state.data)
                is CounterListState.ErrorState -> handleErrorState()
            }
        }

        viewModel.counterUpdateState.observe(this) { state ->
            when (state) {
                is CounterUpdateState.DataState -> handleDataState(state.data)
                is CounterUpdateState.ErrorState -> handleErrorStateUpdate(state)
            }

        }
    }

    private fun handleLoadingState() {
        binding.mainLoading.loading.show()
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = false
        binding.mainError.root.isVisible = false
    }

    private fun handleDataState(counters: List<Counter>) {
        binding.mainLoading.loading.hide()
        binding.mainError.root.isVisible = false

        if (counters.isEmpty()) {
            binding.mainNoCounters.root.isVisible = true
            binding.mainCounter.mainCounterSwipe.isVisible = false
            return
        }
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = true
        binding.mainCounter.counterCount.text = getString(R.string.n_items, counters.size)
        binding.mainCounter.counterSum.text =
            getString(R.string.n_times, counters.sumBy { it.count })
        adapterCounter.updateCounterList(counters)
    }

    private fun handleErrorState() {
        binding.mainLoading.loading.hide()
        binding.mainError.root.isVisible = true
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = false
    }

    private fun handleErrorStateUpdate(state: CounterUpdateState.ErrorState) {
        val target = if (state.increment) {
            state.counter.count + 1
        } else {
            state.counter.count - 1
        }
        val title = getString(
            R.string.error_updating_counter_title,
            state.counter.title,
            target
        )
        val message = if (state.hasInternet) {
            R.string.generic_error_description
        } else {
            R.string.connection_error_description
        }
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }

    private fun setupAdapter(): AdapterCounter {
        return AdapterCounter(
            minusListener = {
                viewModel.updateCounter(it, false)
            },
            plusListener = {
                viewModel.updateCounter(it, true)
            }
        )
    }
}