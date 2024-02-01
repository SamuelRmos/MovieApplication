package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.example.movie.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassicMoviesViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : BaseViewModel() {

    init {
        fetchMovies()
    }

    @VisibleForTesting
    internal fun fetchMovies() {
        viewModelScope.launch {
            executeCall(movieUseCase.executeClassicMovies())
        }
    }
}