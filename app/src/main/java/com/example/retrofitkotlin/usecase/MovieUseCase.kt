package com.example.retrofitkotlin.usecase

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.util.CategoryEnum

interface MovieUseCase {
    suspend fun executePopularMovies(): Either<String, MovieResponse>

    suspend fun executeRatedMovies(): Either<String, MovieResponse>

    suspend fun executeTodayMovies(): Either<String, MovieResponse>

    suspend fun executeClassicMovies(): Either<String, MovieResponse>
}