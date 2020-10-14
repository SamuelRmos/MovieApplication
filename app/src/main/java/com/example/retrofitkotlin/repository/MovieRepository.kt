package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie

interface MovieRepository {
    suspend fun getPopularMovies(isConnected: Boolean): MutableList<TmdMovie>?
}