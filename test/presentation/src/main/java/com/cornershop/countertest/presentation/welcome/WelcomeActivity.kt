package com.cornershop.countertest.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.countertest.presentation.databinding.ActivityWelcomeBinding
import com.cornershop.countertest.presentation.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeContent.buttonStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}