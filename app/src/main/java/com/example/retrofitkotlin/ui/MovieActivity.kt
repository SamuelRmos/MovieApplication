package com.example.retrofitkotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.viewmodel.MovieViewModel

class MovieActivity : AppCompatActivity() {

    private lateinit var tmdbViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        tmdbViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        tmdbViewModel.fetchMovies()

        tmdbViewModel.popularMoviesLiveData.observe(this, Observer {

        })


    }
}
