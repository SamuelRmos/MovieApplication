package com.example.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movie.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : BaseViewModel() {

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            executeCall(movieUseCase.executeRatedMovies())
        }
    }
}