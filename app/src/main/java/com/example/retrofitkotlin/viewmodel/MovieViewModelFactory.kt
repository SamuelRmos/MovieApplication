package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.di.ApiModule
import com.example.retrofitkotlin.di.DaggerApiComponent
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.util.Constants
import javax.inject.Inject

class MovieViewModelFactory : ViewModelProvider.Factory {
    @Inject
    lateinit var movieRepository: MovieRepository

    @Inject
    lateinit var movieDao: MovieDao

    @Inject
    lateinit var movieApplication: MovieApplication

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val apiComponent: ApiComponent = MovieApplication.apiComponent
        apiComponent.inject(this)
        if (modelClass.isAssignableFrom(MovieViewModel::class.java))
            return MovieViewModel(
                movieRepository,
                movieDao,
                movieApplication
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}