package com.example.retrofitkotlin.usecase

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.util.CategoryEnum
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