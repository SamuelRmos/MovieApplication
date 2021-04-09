package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.Movie

interface DetailRepository {
    fun getMovieById(id: Int): Movie
}