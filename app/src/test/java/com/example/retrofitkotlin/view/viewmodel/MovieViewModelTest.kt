@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.retrofitkotlin.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.usecase.MovieUseCase
import com.example.retrofitkotlin.viewmodel.MovieViewModel
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.verify
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mDispatcher = Dispatchers.Unconfined
    private lateinit var viewModel: MovieViewModel
    private val movieUseCase: MovieUseCase = mockk()
    private val response = MovieResponse(listOf())

    @Mock
    lateinit var observer: Observer<MovieViewAction>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MovieViewModel(movieUseCase, mDispatcher, mDispatcher)
        viewModel.actionView.observeForever(observer)
    }

    @Test
    fun `Check State Success - fetchPopularMovies `() {

        val responseOk: Either<String, MovieResponse> = Either.Success(response)

        coEvery { movieUseCase.executePopularMovies(true) } returns responseOk

        viewModel.fetchPopularMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.SuccessPopularMovie::class.java))
        coVerify { movieUseCase.executePopularMovies(true) }
    }

    @Test
    fun `Check State Error - fetchPopularMovies `() {

        val responseNok: Either<String, MovieResponse> = Either.Error("")

        coEvery { movieUseCase.executePopularMovies(true) } returns responseNok

        viewModel.fetchPopularMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.Error::class.java))
        coVerify { movieUseCase.executePopularMovies(true) }
    }

    @Test
    fun `Check State Success - fetchRatedMovies `() {

        val responseOk: Either<String, MovieResponse> = Either.Success(response)

        coEvery { movieUseCase.executeRatedMovies(true) } returns responseOk

        viewModel.fetchRatedMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.SuccessRatedMovie::class.java))
        coVerify { movieUseCase.executeRatedMovies(true) }
    }

    @Test
    fun `Check State Error - fetchRatedMovies `() {

        val responseNok: Either<String, MovieResponse> = Either.Error("")

        coEvery { movieUseCase.executeRatedMovies(true) } returns responseNok

        viewModel.fetchRatedMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.Error::class.java))
        coVerify { movieUseCase.executeRatedMovies(true) }
    }

    @Test
    fun `Check State Success - fetchTodayMovies `() {

        val responseOk: Either<String, MovieResponse> = Either.Success(response)

        coEvery { movieUseCase.executeTodayMovies(true) } returns responseOk

        viewModel.fetchTodayMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.SuccessTodayMovie::class.java))
        coVerify { movieUseCase.executeTodayMovies(true) }
    }

    @Test
    fun `Check State Error - fetchTodayMovies `() {

        val responseNok: Either<String, MovieResponse> = Either.Error("")

        coEvery { movieUseCase.executeTodayMovies(true) } returns responseNok

        viewModel.fetchTodayMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.Error::class.java))
        coVerify { movieUseCase.executeTodayMovies(true) }
    }

    @Test
    fun `Check State Success - fetchClassicMovies `() {

        val responseOk: Either<String, MovieResponse> = Either.Success(response)

        coEvery { movieUseCase.executeClassicMovies(true) } returns responseOk

        viewModel.fetchClassicMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.SuccessClassicMovie::class.java))
        coVerify { movieUseCase.executeClassicMovies(true) }
    }

    @Test
    fun `Check State Error - fetchClassicMovies `() {

        val responseNok: Either<String, MovieResponse> = Either.Error("")

        coEvery { movieUseCase.executeClassicMovies(true) } returns responseNok

        viewModel.fetchClassicMovies(true)

        assertNotNull(viewModel.actionView.value)
        verify(observer, atLeast(2)).onChanged(isA(MovieViewAction.Loading::class.java))
        verify(observer).onChanged(isA(MovieViewAction.Error::class.java))
        coVerify { movieUseCase.executeClassicMovies(true) }
    }
}