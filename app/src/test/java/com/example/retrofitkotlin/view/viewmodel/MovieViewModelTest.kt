@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.view.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovieList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieViewModelTest {

    //region constants

    //end region constants

    //region helper fields

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val mDispatcher = Dispatchers.Unconfined

    private lateinit var sut: MovieViewModel
    private val mMovieRepo = mockk<MovieRepositoryImpl>(relaxed = true)
    private val mApplication = mockk<MovieApplication>(relaxed = true)
    private val manager = mockk<ConnectivityManager>(relaxed = true)

    // end region helper fields

    @Before
    fun setup() {
    }

    @Test
    fun `movieViewModel fetchData`() = runBlocking {

        coEvery { mMovieRepo.getPopularMovies(true) } returns mockMovieList()
        every { mApplication.getSystemService(Context.CONNECTIVITY_SERVICE) } returns manager
        every { manager.activeNetworkInfo!!.isConnectedOrConnecting } returns true

        sut = MovieViewModel(mMovieRepo, mApplication, mDispatcher, mDispatcher)
        sut.fetchMovies()
        sut.popularMoviesLiveData.observeForever {  }

        assertNotNull(sut.popularMoviesLiveData.value)
    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}