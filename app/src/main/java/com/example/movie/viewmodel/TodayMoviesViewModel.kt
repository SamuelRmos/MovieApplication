package com.example.movie.viewmodel

import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    init {
        fetchMovies(1)
    }

    fun fetchMovies(page: Int) {
        executeCall {
            movieRepository.getListTodayMovies(page)
        }
    }
}