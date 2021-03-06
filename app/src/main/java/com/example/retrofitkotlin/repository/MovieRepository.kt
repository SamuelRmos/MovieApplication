package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.util.CategoryEnum

interface MovieRepository {
    suspend fun getListPopularMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun getListRatedMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun getListTodayMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun getListClassicMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    fun getMoviePoster(): MutableList<Movie>
}