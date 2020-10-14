package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie

interface DetailRepository {
    fun getMovieById(id: Int): TmdMovie
}