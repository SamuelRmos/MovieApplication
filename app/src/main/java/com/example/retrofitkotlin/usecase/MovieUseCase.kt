package com.example.retrofitkotlin.usecase

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.util.CategoryEnum

interface MovieUseCase {
    suspend fun executePopularMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun executeRatedMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun executeTodayMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>

    suspend fun executeClassicMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse>
}