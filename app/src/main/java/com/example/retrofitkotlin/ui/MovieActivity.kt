package com.example.retrofitkotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitkotlin.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
