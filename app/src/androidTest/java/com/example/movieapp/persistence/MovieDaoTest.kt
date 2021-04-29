package com.example.movieapp.persistence

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.factory.BufferFactory
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest : AppDataBaseTest() {

    private lateinit var movieDao: MovieDao

    @Before
    fun init() {
        movieDao = appDataBase.movieDao()
    }

    @Test
    fun movieDao_insertMovie() {

        val cachedMovie = BufferFactory.makeCachedMovie()
        movieDao.insertMovieList(cachedMovie)

        val movieList = movieDao.getMovieList()
        assertThat(movieList[0].id, CoreMatchers.`is`(cachedMovie[0].id))

    }
}