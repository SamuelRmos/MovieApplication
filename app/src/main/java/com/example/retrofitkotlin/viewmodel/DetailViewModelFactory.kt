package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepository
import javax.inject.Inject

class DetailViewModelFactory: ViewModelProvider.Factory {

    @Inject
    lateinit var repository: DetailRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val apiComponent: ApiComponent = MovieApplication.apiComponent
        apiComponent.inject(this)
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java))
            return MovieDetailViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}