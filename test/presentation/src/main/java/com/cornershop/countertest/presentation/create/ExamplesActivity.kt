package com.cornershop.countertest.presentation.create

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.countertest.presentation.databinding.ActivityExamplesBinding

class ExamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityExamplesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)

        binding.examplesDrinks.exampleClickListener = exampleListener
        binding.examplesFoods.exampleClickListener = exampleListener
        binding.examplesMisc.exampleClickListener = exampleListener
    }

    private val exampleListener = TemplateExampleListView.ExampleClickListener { example ->
        setResult(
            RESULT_CODE,
            Intent().apply { putExtra(RESULT_EXAMPLE_KEY, example) }
        )
        finish()
    }

    companion object {
        const val RESULT_CODE = 42
        const val RESULT_EXAMPLE_KEY = "RESULT_EXAMPLE_KEY"
    }
}