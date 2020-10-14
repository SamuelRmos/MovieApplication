package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.persistence.MovieDao

class DetailRepositoryImpl constructor(private val movieDao: MovieDao) : DetailRepository {

    override fun getMovieById(id: Int) = movieDao.getMovie(id)
}