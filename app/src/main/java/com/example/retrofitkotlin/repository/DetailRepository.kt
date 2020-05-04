package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.persistence.MovieDao
import javax.inject.Inject

class DetailRepository: Repository {
    @Inject
    lateinit var movieDao: MovieDao

    init {
        val apiComponent: ApiComponent = MovieApplication.apiComponent
        apiComponent.inject(this)
    }

    override var isLoading = false

    fun getMovieById(id: Int) = movieDao.getMovie(id)
}