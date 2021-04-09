package com.example.retrofitkotlin.usecase

import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
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

        coEvery { movieRepository.getListPopularMovies(true) } returns responseOk

        useCase.executePopularMovies(true)

        coVerify { movieRepository.getListPopularMovies(true) }
    }

    @Test
    fun `Check executeRatedMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListRatedMovies(true) } returns responseOk

        useCase.executeRatedMovies(true)

        coVerify { movieRepository.getListRatedMovies(true) }
    }

    @Test
    fun `Check executeTodayMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListTodayMovies(true) } returns responseOk

        useCase.executeTodayMovies(true)

        coVerify { movieRepository.getListTodayMovies(true) }
    }

    @Test
    fun `Check executeClassicMovies`() = runBlocking {

        val responseOk = Either.Success(response)

        coEvery { movieRepository.getListClassicMovies(true) } returns responseOk

        useCase.executeClassicMovies(true)

        coVerify { movieRepository.getListClassicMovies(true) }
    }
}