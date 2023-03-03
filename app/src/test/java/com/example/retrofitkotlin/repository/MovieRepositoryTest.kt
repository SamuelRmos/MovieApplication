package com.example.retrofitkotlin.repository

import com.example.commons.service.ConnectionService
import com.example.retrofitkotlin.base.BaseTest
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.network.MovieApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovieList
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_OK

class MovieRepositoryTest : BaseTest() {

    @MockK
    private lateinit var movieDao: MovieDao

    @MockK
    private lateinit var movieApi: MovieApi

    @MockK
    private lateinit var mockResponse: Response<MovieResponse>

    @MockK
    private lateinit var connectionService: ConnectionService

    private lateinit var movieRepositoryImpl: MovieRepositoryImpl

    @Before
    fun setup() {
        init(this, relaxed = true)
        movieRepositoryImpl = MovieRepositoryImpl(movieApi, movieDao, connectionService)
        every { connectionService.isNetworkAvailable() } returns true
        super.setUp()
    }

    @Test
    fun `Check getListPopularMovies is connected return data success`() = runBlocking {

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

        coEvery { movieApi.getPopularMovieAsync().await() } returns mockResponse

        val dataReceived = movieRepositoryImpl.getListPopularMovies()

        assertNotNull(dataReceived)
        assert(dataReceived.isSuccess)
    }

    @Test
    fun `Check getListPopularMovies is connected return data error`() = runBlocking {

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

        coEvery { movieApi.getPopularMovieAsync().await() } returns mockResponse

        val dataReceived = movieRepositoryImpl.getListPopularMovies()

        assertNotNull(dataReceived)
        assert(dataReceived.isError)
    }

    @Test
    fun `Check getListRatedMovies is connected and getListMovies retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getRatedMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListRatedMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListRatedMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getRatedMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListRatedMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListTodayMovies is connected and retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListTodayMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListTodayMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getTodayMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListTodayMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListClassicMovies is connected and retrieve data success`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.body() } returns MovieResponse(listOf(mockMovie()))

            coEvery { movieApi.getClassicMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListClassicMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isSuccess)
        }

    @Test
    fun `Check getListClassicMovies is connected and retrieve data error`() =
        runBlocking {

            every { mockResponse.isSuccessful } returns false
            coEvery { movieApi.getClassicMovieAsync().await() } returns mockResponse

            val dataReceived = movieRepositoryImpl.getListClassicMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListRatedMovies is not connected and retrieve data error`() =
        runBlocking {
            every { connectionService.isNetworkAvailable() } returns false
            val dataReceived = movieRepositoryImpl.getListRatedMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }


    @Test
    fun `Check getListClassicMovies is not connected and retrieve data error`() =
        runBlocking {
            every { connectionService.isNetworkAvailable() } returns false
            val dataReceived = movieRepositoryImpl.getListClassicMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListPopularMovies is not connected and retrieve data error`() =
        runBlocking {
            every { connectionService.isNetworkAvailable() } returns false
            val dataReceived = movieRepositoryImpl.getListPopularMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `Check getListTodayMovies is not connected and retrieve data error`() =
        runBlocking {
            every { connectionService.isNetworkAvailable() } returns false
            val dataReceived = movieRepositoryImpl.getListTodayMovies()

            assertNotNull(dataReceived)
            assert(dataReceived.isError)
        }

    @Test
    fun `movieRepoTest is connected and getListMovies retrieve data success RATED`() =
        runBlocking {

            mockNetworkResponseWithFileContent("movie_list", HTTP_OK)

            every { movieDao.getMovieList() } returns mutableListOf()
            movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao, connectionService)

        }

    @Test
    fun `getMoviePoster retrieve database success`() = runBlocking {

        every { movieDao.getMovieList() } returns mockMovieList()
        movieRepositoryImpl = MovieRepositoryImpl(createApi(), movieDao, connectionService)

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

            movieRepositoryImpl = MovieRepositoryImpl(movieApi, movieDao, connectionService)
        }
}