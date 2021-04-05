package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.persistence.MovieDao
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(private val movieDao: MovieDao) : DetailRepository {

    override fun getMovieById(id: Int) = movieDao.getMovie(id)
}