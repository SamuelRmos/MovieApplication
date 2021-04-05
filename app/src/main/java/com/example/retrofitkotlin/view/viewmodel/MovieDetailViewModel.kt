package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    fun getMovie(id: Int) = detailRepository.getMovieById(id)
}