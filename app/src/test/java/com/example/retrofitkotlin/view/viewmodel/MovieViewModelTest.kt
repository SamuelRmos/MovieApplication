@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import com.example.retrofitkotlin.util.CategoryEnum
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovieList
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val mDispatcher = Dispatchers.Unconfined
    private lateinit var viewModel: MovieViewModel
    private val mMovieRepo = mockk<MovieRepositoryImpl>(relaxed = true)

    @Before
    fun setup() {
        viewModel = MovieViewModel(mMovieRepo, mDispatcher, mDispatcher)
    }

    @Test
    fun `fetchMovies success`() {

        coEvery {
            mMovieRepo.getListMovies(
                true, CategoryEnum.POPULAR
            )
        } returns mockMovieList()


        val dataReceived = viewModel.fetchMovies(CategoryEnum.POPULAR, true)
        val data = mockMovieList()

        assertNotNull(dataReceived)
        Assert.assertEquals(dataReceived.value?.get(0)?.id, data[0].id)
        Assert.assertEquals(dataReceived.value?.get(0)?.title, data[0].title)
        Assert.assertEquals(dataReceived.value?.get(0)?.overview, data[0].overview)
    }

    @Test
    fun `getListMovies success`() {

        coEvery {
            mMovieRepo.getMoviePoster()
        } returns mockMovieList()

        val dataReceived = viewModel.getListMovies()
        val data = mockMovieList()

        assertNotNull(dataReceived)
        Assert.assertEquals(dataReceived[0].id, data[0].id)
        Assert.assertEquals(dataReceived[0].title, data[0].title)
        Assert.assertEquals(dataReceived[0].overview, data[0].overview)
    }

}