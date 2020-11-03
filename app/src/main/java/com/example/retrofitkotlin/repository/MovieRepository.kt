package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.util.CategoryEnum

interface MovieRepository {
    suspend fun getListMovies(isConnected: Boolean, id: CategoryEnum): MutableList<TmdMovie>?
}