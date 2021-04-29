package com.example.movieapp.usecase

import com.example.movieapp.functional.Either
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.repository.MovieRepository
import javax.inject.Inject

class MovieUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) :
    MovieUseCase {

    override suspend fun executePopularMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> = movieRepository.getListPopularMovies(isConnected)

    override suspend fun executeRatedMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> = movieRepository.getListRatedMovies(isConnected)

    override suspend fun executeTodayMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> = movieRepository.getListTodayMovies(isConnected)

    override suspend fun executeClassicMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> = movieRepository.getListClassicMovies(isConnected)
}