package com.cornershop.countertest.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.countertest.domain.model.Counter
import com.cornershop.countertest.domain.model.CounterListState
import com.cornershop.countertest.domain.model.CounterSelectedState
import com.cornershop.countertest.domain.model.CounterUpdateState
import com.cornershop.countertest.presentation.R
import com.cornershop.countertest.presentation.create.CreateCounterActivity
import com.cornershop.countertest.presentation.databinding.ActivityMainBinding
import com.cornershop.countertest.presentation.main.adapter.AdapterCounter
import com.cornershop.countertest.presentation.main.adapter.AdapterSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val adapterCounter = setupAdapter()
    private val adapterSelected by lazy { setupSelectedAdapter() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainCounter.mainCounterList.adapter = adapterCounter
        binding.addCounterButton.setOnClickListener {
            startActivity(Intent(this, CreateCounterActivity::class.java))
        }
        setupSelectedMenu()

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
                is CounterUpdateState.ErrorState -> handleErrorStateUpdate(state)
            }
        }

        viewModel.selectedState.observe(this) { state ->
            when (state) {
                is CounterSelectedState.ChangeState -> handleSelectedChangeState(state)
                is CounterSelectedState.SuccessState -> handleSelectedSuccessState()
                is CounterSelectedState.DataState -> handleSelectedDataState(state)
                is CounterSelectedState.DeleteErrorState -> handleSelectedErrorState(state)
                is CounterSelectedState.DeleteState -> handleSelectDeleteState(state)
            }
        }
    }

    //region CounterListState
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
        if (binding.mainCounter.mainCounterList.adapter !is AdapterCounter) {
            binding.mainCounter.mainCounterList.adapter = adapterCounter
        }
        adapterCounter.updateCounterList(counters)
    }

    private fun handleErrorState() {
        binding.mainLoading.loading.hide()
        binding.mainError.root.isVisible = true
        binding.mainNoCounters.root.isVisible = false
        binding.mainCounter.mainCounterSwipe.isVisible = false
    }
    //endregion

    //region CounterUpdateState
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
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(errorMessage(state.hasInternet))
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }
    //endregion

    //region CounterSelectState
    private fun handleSelectedChangeState(state: CounterSelectedState.ChangeState) {
        binding.mainSelected.root.isVisible = true
        binding.mainCounter.mainCounterList.adapter = adapterSelected
        binding.addCounterButton
            .animate()
            .translationY(binding.addCounterButton.height * 3f)
            .start()
        binding.search.animate()
            .translationY(binding.search.height * -3f)
            .start()
        binding.mainSelected.selectedToolbar.title = getString(R.string.n_selected, 1)
        adapterSelected.updateCounterList(state.data)
    }

    private fun handleSelectedDataState(state: CounterSelectedState.DataState) {
        adapterSelected.updateCounterList(state.data)
        binding.mainSelected.selectedToolbar.title = getString(
            R.string.n_selected,
            state.data.filter { it.selected }.size
        )
    }

    private fun handleSelectedSuccessState() {
        binding.mainSelected.root.isVisible = false
        binding.addCounterButton.animate().translationY(0f).start()
        binding.search.animate().translationY(0f).start()
        viewModel.updateCounterList()
    }

    private fun handleSelectedErrorState(state: CounterSelectedState.DeleteErrorState) {
        AlertDialog.Builder(this)
            .setTitle(R.string.error_deleting_counter_title)
            .setMessage(errorMessage(state.hasInternet))
            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.cancelDelete(state.data) }
            .create()
            .show()
    }

    private fun handleSelectDeleteState(state: CounterSelectedState.DeleteState): Boolean {
        val selectedList = state.data.filter { it.selected }
        val message = if (selectedList.size == 1) {
            getString(R.string.delete_x_question, selectedList[0].counter.title)
        } else {
            getString(R.string.delete_n_questions, selectedList.size)
        }

        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.delete) { _, _ -> viewModel.deleteCounters(state.data) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> viewModel.cancelDelete(state.data) }
            .create()
            .show()

        return true
    }

    private fun setupAdapter(): AdapterCounter {
        return AdapterCounter(
            minusListener = {
                viewModel.updateCounter(it, false)
            },
            plusListener = {
                viewModel.updateCounter(it, true)
            },
            selectedListener = { counter ->
                viewModel.changeStateToSelect(counter)
            }
        )
    }

    private fun setupSelectedAdapter(): AdapterSelected {
        return AdapterSelected { counter ->
            viewModel.selectCounter(counter)
        }
    }

    private fun setupSelectedMenu() {
        binding.mainSelected.selectedToolbar.inflateMenu(R.menu.menu_main)
        binding.mainSelected.selectedToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> shareCounters()
                R.id.delete -> {
                    viewModel.selectDelete()
                    true
                }
                else -> false
            }
        }
    }

    private fun shareCounters(): Boolean {

        return true
    }
//endregion

    private fun errorMessage(hasInternet: Boolean): Int = if (hasInternet) {
        R.string.generic_error_description
    } else {
        R.string.connection_error_description
    }
}