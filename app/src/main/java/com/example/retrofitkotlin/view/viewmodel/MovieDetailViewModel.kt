package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.repository.DetailRepositoryImpl

class MovieDetailViewModel constructor(private val repositoryImpl: DetailRepositoryImpl) : ViewModel() {

    fun getMovie(id: Int) = repositoryImpl.getMovieById(id)

}