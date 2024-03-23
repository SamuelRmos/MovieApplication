package com.example.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        executeCall {
            movieRepository.getListTodayMovies()
        }
    }
}