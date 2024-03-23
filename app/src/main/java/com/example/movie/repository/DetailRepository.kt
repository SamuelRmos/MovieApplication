package com.example.movie.repository

import com.example.commons.functional.Either
import com.example.movie.model.MovieCredits
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun getMovieCredits(id: Int): Flow<Either<String, MovieCredits>>
}