package com.example.movieapp.repository

import com.example.movieapp.functional.Either
import com.example.movieapp.model.Movie
import com.example.movieapp.model.MovieResponse

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