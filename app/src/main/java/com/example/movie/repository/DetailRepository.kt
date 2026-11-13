package com.example.movie.repository

import com.example.movie.model.MovieCredits
import com.example.movie.model.VideosList
import com.example.movie.ui.details.DetailRequestState
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getMovieCredits(id: Int): Flow<DetailRequestState<MovieCredits>>
    fun getMovieVideo(id: Int): Flow<DetailRequestState<VideosList>>
}
