package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.repository.DetailRepository

class MovieDetailViewModel constructor(private val repository: DetailRepository) : ViewModel() {

    fun getMovie(id: Int) = repository.getMovieById(id)
}