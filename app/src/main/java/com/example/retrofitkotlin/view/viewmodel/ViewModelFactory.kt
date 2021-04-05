package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var movieRepository: MovieRepository

    @Inject
    lateinit var movieApplication: MovieApplication

    @Inject
    lateinit var detailRepository: DetailRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MovieApplication.apiComponent.inject(this)
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) ->
                MovieViewModel(
                    movieRepository,
                    movieApplication,
                    Dispatchers.Main,
                    Dispatchers.IO
                ) as T

            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) ->
                MovieDetailViewModel(detailRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}