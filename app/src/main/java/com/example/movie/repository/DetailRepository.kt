package com.example.movie.repository

import com.example.movie.model.Movie

interface DetailRepository {
    fun getMovieById(id: Int): Movie
}