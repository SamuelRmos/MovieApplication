@file:Suppress("DEPRECATION")

package com.example.movieapp.repository

import com.example.movieapp.base.BaseTest
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.network.MovieApi
import com.example.movieapp.persistence.MovieDao
import com.example.movieapp.utils.MockTestUtil.mockMovie
import com.example.movieapp.utils.MockTestUtil.mockMovieList
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
import retrofit2.Response
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class MovieRepositoryTest : BaseTest() {

    private lateinit var movieRepositoryImpl: MovieRepositoryImpl
    private val movieDao = mockk<MovieDao>(relaxed = true)
    private val movieApi = mockk<MovieApi>()
    private val mockResponse = mockk<Response<MovieResponse>>()

    @Before
    fun setup() {
        movieRepositoryImpl = MovieRepositoryImpl(movieApi, movieDao)
        super.setUp()
    }

    @Test
    fun `Check getListPopularMovies is connected return data success`() = runBlocking {

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

        coEvery { movieApi.getPopularMovieAsync().await() } returns mockResponse

        val dataReceived = movieRepositoryImpl.getListPopularMovies(true)

        assertNotNull(dataReceived)
        assert(dataReceived.isSuccess)
    }

    @Test
    fun `Check getListPopularMovies is connected return data error`() = runBlocking {

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

        coEvery { movieApi.getPopularMovieAsync().await() } returns mockResponse

        val dataReceived = movieRepositoryImpl.getListPopularMovies(true)

        assertNotNull(dataReceived)
        assert(dataReceived.isError)
    }

    @Test
    fun `Check getListRatedMovies is connected and getListMovies retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getRatedMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListRatedMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListRatedMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getRatedMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListRatedMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListTodayMovies is connected and retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListTodayMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListTodayMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListTodayMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListClassicMovies is connected and retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getClassicMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListClassicMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListClassicMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getClassicMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListClassicMovies(true)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListRatedMovies is not connected and retrieve data error`() =
        runBlocking {
            val dataReceived = movieRepositoryImpl.getListRatedMovies(false)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }


    @Test
    fun `Check getListClassicMovies is not connected and retrieve data error`() =
        runBlocking {
            val dataReceived = movieRepositoryImpl.getListClassicMovies(false)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListPopularMovies is not connected and retrieve data error`() =
        runBlocking {
            val dataReceived = movieRepositoryImpl.getListPopularMovies(false)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListTodayMovies is not connected and retrieve data error`() =
        runBlocking {
            val dataReceived = movieRepositoryImpl.getListTodayMovies(false)

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `movieRepoTest is connected and getListMovies retrieve data success RATED`() =
        runBlocking {

            mockNetworkResponseWithFileContent("movie_list", HttpURLConnection.HTTP_OK)

            every { movieDao.getMovieList() } returns mutableListOf()
            movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao)

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

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse
            every { movieDao.insertMovieList(any()) } returns Unit

            movieRepositoryImpl = MovieRepositoryImpl(movieApi, movieDao)
        }
}