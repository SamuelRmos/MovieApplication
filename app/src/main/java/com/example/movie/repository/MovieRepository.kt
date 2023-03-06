package com.example.movie.repository

import com.example.commons.functional.Either
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse

interface MovieRepository {
    suspend fun getListPopularMovies(): Either<String, MovieResponse>

    suspend fun getListRatedMovies(): Either<String, MovieResponse>

    suspend fun getListTodayMovies(): Either<String, MovieResponse>

    suspend fun getListClassicMovies(): Either<String, MovieResponse>

    fun getMoviePoster(): MutableList<Movie>
}