package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassicMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {
    init {
        fetchMovies()
    }

    @VisibleForTesting
    internal fun fetchMovies() {
        executeCall {
            movieRepository.getListClassicMovies()
        }
    }
}