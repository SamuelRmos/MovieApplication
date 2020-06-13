package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var movieRepository: MovieRepository

    @Inject
    lateinit var movieApplication: MovieApplication

    @Inject
    lateinit var repository: DetailRepository

    init {
        val apiComponent: ApiComponent = MovieApplication.apiComponent
        apiComponent.inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) ->
                MovieViewModel(
                    movieRepository,
                    movieApplication,
                    Dispatchers.Main,
                    Dispatchers.IO
                ) as T

            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) ->
                MovieDetailViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}