@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.base.BaseTest
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.util.CategoryEnum
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovieList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class MovieRepositoryTest : BaseTest() {

    //region constants

    //end region constants

    //region helper fields

    private lateinit var sut: MovieRepositoryImpl
    private val movieDao = mockk<MovieDao>(relaxed = true)

    // end region helper fields

    @Before
    fun setup() {
        super.setUp()
    }

    @Test
    fun `movieRepoTest movieApi retrieveData`() = runBlocking {

        mockNetworkResponseWithFileContent("movie_list", HttpURLConnection.HTTP_OK)

        coEvery { movieDao.getMovieList() } returns mutableListOf()
        sut = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = sut.getListMovies(true, CategoryEnum.POPULAR)
        val data = mockMovieList()

        assertNotNull(dataReceived)
        assertEquals(dataReceived!![0].id, data[0].id)
        assertEquals(dataReceived[0].title, data[0].title)
        assertEquals(dataReceived[0].overview, data[0].overview)
    }

    @Test
    fun `movieRepoTest movieDao retrieveData`() = runBlocking {

        every { movieDao.getMovieList() } returns mockMovieList()
        sut = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = sut.getListMovies(false, CategoryEnum.POPULAR)
        val data = mockMovieList()

        assertNotNull(dataReceived)
        assertEquals(dataReceived!![0].id, data[0].id)
        assertEquals(dataReceived[0].title, data[0].title)
        assertEquals(dataReceived[0].overview, data[0].overview)
    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}