package com.example.movie.ui

import com.example.movie.model.Movie

sealed class RequestState {
    object Loading : RequestState()
    data class Success(val data: List<Movie>) : RequestState()
    data class Error(val message: String) : RequestState()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

    fun getMovieList(): List<Movie> = (this as Success).data
}