package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.retrofitkotlin.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayMoviesViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : BaseViewModel() {

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            executeCall(movieUseCase.executeTodayMovies())
        }
    }
}