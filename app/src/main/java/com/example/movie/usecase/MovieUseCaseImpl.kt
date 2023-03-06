package com.example.movie.usecase

import com.example.commons.functional.Either
import com.example.movie.model.MovieResponse
import com.example.movie.repository.MovieRepository
import javax.inject.Inject

class MovieUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) :
    MovieUseCase {
    override suspend fun executePopularMovies(): Either<String, MovieResponse> =
        movieRepository.getListPopularMovies()

    override suspend fun executeRatedMovies(): Either<String, MovieResponse> =
        movieRepository.getListRatedMovies()

    override suspend fun executeTodayMovies(): Either<String, MovieResponse> =
        movieRepository.getListTodayMovies()

    override suspend fun executeClassicMovies(): Either<String, MovieResponse> =
        movieRepository.getListClassicMovies()
}