package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @Inject
    lateinit var movieApplication: MovieApplication

    @Inject
    lateinit var repositoryImpl: DetailRepositoryImpl

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MovieApplication.apiComponent.inject(this)
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) ->
                MovieViewModel(
                    movieRepositoryImpl,
                    movieApplication,
                    Dispatchers.Main,
                    Dispatchers.IO
                ) as T

            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) ->
                MovieDetailViewModel(repositoryImpl) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}