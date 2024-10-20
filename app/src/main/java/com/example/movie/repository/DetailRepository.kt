package com.example.movie.repository

import com.example.movie.ui.details.DetailRequestState
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    suspend fun getMovieCredits(id: Int): Flow<DetailRequestState>
}