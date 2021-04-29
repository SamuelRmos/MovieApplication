package com.example.movieapp.usecase

import com.example.movieapp.functional.Either
import com.example.movieapp.model.MovieResponse

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