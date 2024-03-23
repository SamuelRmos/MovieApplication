package com.example.movie.viewmodel

import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        executeCall {
            movieRepository.getListRatedMovies()
        }
    }
}