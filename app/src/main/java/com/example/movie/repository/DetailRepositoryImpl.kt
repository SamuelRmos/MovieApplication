package com.example.movie.repository

import com.example.movie.persistence.MovieDao
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(private val movieDao: MovieDao) : DetailRepository {

    override fun getMovieById(id: Int) = movieDao.getMovie(id)
}