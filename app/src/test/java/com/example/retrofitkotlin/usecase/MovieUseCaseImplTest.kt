package com.example.retrofitkotlin.usecase

import com.example.commons.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MovieUseCaseImplTest {

    private val movieRepository = mockk<MovieRepositoryImpl>(relaxed = true)
    private lateinit var useCase: MovieUseCaseImpl
    private val response = MovieResponse(listOf())

    @Before
    fun setup() {
        useCase = MovieUseCaseImpl(movieRepository)
    }

    @Test
    fun `Check executePopularMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListPopularMovies() } returns responseOk

        useCase.executePopularMovies()

        coVerify { movieRepository.getListPopularMovies() }
    }

    @Test
    fun `Check executeRatedMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListRatedMovies() } returns responseOk

        useCase.executeRatedMovies()

        coVerify { movieRepository.getListRatedMovies() }
    }

    @Test
    fun `Check executeTodayMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListTodayMovies() } returns responseOk

        useCase.executeTodayMovies()

        coVerify { movieRepository.getListTodayMovies() }
    }

    @Test
    fun `Check executeClassicMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListClassicMovies() } returns responseOk

        useCase.executeClassicMovies()

        coVerify { movieRepository.getListClassicMovies() }
    }
}