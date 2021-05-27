package com.cornershop.countertest.presentation.create

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cornershop.countertest.domain.model.CounterSaveState
import com.cornershop.countertest.presentation.R
import com.cornershop.countertest.presentation.databinding.ActivityCreateCounterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCounterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCounterBinding
    private val viewModel: CreateCounterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            viewModel.saveCounter(binding.title.text.toString())
        }
        binding.close.setOnClickListener { finish() }
        observe()
    }

    private fun observe() {
        viewModel.counterSaveState.observe(this) { state ->
            when(state) {
                is CounterSaveState.Loading -> {
                    binding.saveButton.isVisible = false
                    binding.saveLoad.isVisible = true
                }
                is CounterSaveState.Error -> {
                    handleErrorState(state)
                }
                is CounterSaveState.SaveSuccess -> {
                    finish()
                }
            }
        }
    }

    private fun handleErrorState(state: CounterSaveState.Error) {
        binding.saveButton.isVisible = true
        binding.saveLoad.isVisible = false

        val message = if (state.hasInternet) {
            R.string.generic_error_description
        } else {
            R.string.connection_error_description
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.error_creating_counter_title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }
}