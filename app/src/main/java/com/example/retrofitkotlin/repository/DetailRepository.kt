package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.persistence.MovieDao

class DetailRepository constructor(private val movieDao: MovieDao): Repository {

    override var isLoading = false

    fun getMovieById(id: Int) = movieDao.getMovie(id)
}