package com.example.movieapp.repository

import com.example.movieapp.persistence.MovieDao
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(private val movieDao: MovieDao) : DetailRepository {

    override fun getMovieById(id: Int) = movieDao.getMovie(id)
}