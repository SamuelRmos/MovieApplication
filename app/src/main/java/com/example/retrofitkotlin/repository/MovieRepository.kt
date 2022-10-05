package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.util.CategoryEnum

interface MovieRepository {
    suspend fun getListPopularMovies(): Either<String, MovieResponse>

    suspend fun getListRatedMovies(): Either<String, MovieResponse>

    suspend fun getListTodayMovies(): Either<String, MovieResponse>

    suspend fun getListClassicMovies(): Either<String, MovieResponse>

    fun getMoviePoster(): MutableList<Movie>
}