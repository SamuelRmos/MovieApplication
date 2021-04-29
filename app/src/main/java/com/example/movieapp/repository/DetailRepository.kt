package com.example.movieapp.repository

import com.example.movieapp.model.Movie

interface DetailRepository {
    fun getMovieById(id: Int): Movie
}