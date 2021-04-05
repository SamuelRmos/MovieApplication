@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.base.BaseTest
import com.example.retrofitkotlin.model.TmdbMovieResponse
import com.example.retrofitkotlin.network.MovieApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.util.CategoryEnum
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovieList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class MovieRepositoryTest : BaseTest() {

    //region constants

    //end region constants

    //region helper fields

    private lateinit var movieRepositoryImpl: MovieRepositoryImpl
    private val movieDao = mockk<MovieDao>(relaxed = true)
    private val movieApi = mockk<MovieApi>()

    // end region helper fields

    @Before
    fun setup() {
        super.setUp()
    }

    @Test
    fun `movieRepoTest is connected and getListMovies retrieve data success POPULAR `() =
        runBlocking {

            mockNetworkResponseWithFileContent("movie_list", HttpURLConnection.HTTP_OK)

            every { movieDao.getMovieList() } returns mutableListOf()
            movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

            val dataReceived = movieRepositoryImpl.getListMovies(true, CategoryEnum.POPULAR)
            val data = mockMovieList()

            assertNotNull(dataReceived)
            assertEquals(dataReceived!![0].id, data[0].id)
            assertEquals(dataReceived[0].title, data[0].title)
            assertEquals(dataReceived[0].overview, data[0].overview)
        }

    @Test
    fun `movieRepoTest is connected and getListMovies retrieve data success RATED`() = runBlocking {

        mockNetworkResponseWithFileContent("movie_list", HttpURLConnection.HTTP_OK)

        every { movieDao.getMovieList() } returns mutableListOf()
        movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = movieRepositoryImpl.getListMovies(true, CategoryEnum.RATED)
        val data = mockMovieList()

        assertNotNull(dataReceived)
        assertEquals(dataReceived!![0].id, data[0].id)
        assertEquals(dataReceived[0].title, data[0].title)
        assertEquals(dataReceived[0].overview, data[0].overview)
    }

    @Test
    fun `movieRepoTest is connected and getListMovies retrieve data success CLASSIC`() = runBlocking {

        mockNetworkResponseWithFileContent("movie_list", HttpURLConnection.HTTP_OK)

        every { movieDao.getMovieList() } returns mutableListOf()
        movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = movieRepositoryImpl.getListMovies(true, CategoryEnum.CLASSIC)
        val data = mockMovieList()

        assertNotNull(dataReceived)
        assertEquals(dataReceived!![0].id, data[0].id)
        assertEquals(dataReceived[0].title, data[0].title)
        assertEquals(dataReceived[0].overview, data[0].overview)
    }

    @Test
    fun `movieRepoTest not connected and getListMovies retrieve data fail`() = runBlocking {

        movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = movieRepositoryImpl.getListMovies(false, CategoryEnum.POPULAR)

        assertNull(dataReceived)
    }

    @Test
    fun `getMoviePoster retrieve database success`() = runBlocking {

        every { movieDao.getMovieList() } returns mockMovieList()
        movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

        val dataReceived = movieRepositoryImpl.getMoviePoster()
        val data = mockMovieList()

        assertNotNull(dataReceived)
        assertEquals(dataReceived[0].id, data[0].id)
        assertEquals(dataReceived[0].title, data[0].title)
        assertEquals(dataReceived[0].overview, data[0].overview)
    }

    @Test
    fun `getListMovies is connected and getDataMovie id TODAY insert data in database and return data success`() =
        runBlocking {

            val mockResponse = mockk<Response<TmdbMovieResponse>>()

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns TmdbMovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse
            every { movieDao.insertMovieList(any()) } returns Unit

            movieRepositoryImpl = MovieRepositoryImpl(movieApi, movieDao)

            val dataReceived = movieRepositoryImpl.getListMovies(true, CategoryEnum.TODAY)
            val data = mockMovieList()

            assertNotNull(dataReceived)
            verify { movieDao.insertMovieList(any()) }
            assertEquals(dataReceived!![0].id, data[0].id)
            assertEquals(dataReceived[0].title, data[0].title)
            assertEquals(dataReceived[0].overview, data[0].overview)
        }


}