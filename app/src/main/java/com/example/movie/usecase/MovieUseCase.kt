package com.example.movie.usecase

import com.example.commons.functional.Either
import com.example.movie.model.MovieResponse

interface MovieUseCase {
    suspend fun executePopularMovies(): Either<String, MovieResponse>

    suspend fun executeRatedMovies(): Either<String, MovieResponse>

    suspend fun executeTodayMovies(): Either<String, MovieResponse>

    suspend fun executeClassicMovies(): Either<String, MovieResponse>
}