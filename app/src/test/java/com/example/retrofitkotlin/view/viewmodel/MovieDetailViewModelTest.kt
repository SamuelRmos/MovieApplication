package com.example.retrofitkotlin.view.viewmodel

import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import com.example.retrofitkotlin.viewmodel.MovieDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class MovieDetailViewModelTest {
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private val mDetailRepository = mockk<DetailRepositoryImpl>(relaxed = true)

    @Before
    fun setup() {
        movieDetailViewModel = MovieDetailViewModel(mDetailRepository)
    }

    @Test
    fun `getMovieById return data from database when Id passed is salved`() {
        val movie = mockMovie()
        val id = 419704
        every { mDetailRepository.getMovieById(id) } returns movie

        val dataReceived = movieDetailViewModel.getMovie(id)

        verify { mDetailRepository.getMovieById(id) }
        assertNotNull(dataReceived)
        assertEquals(movie.id, dataReceived.id)
        assertEquals(movie.title, dataReceived.title)
        assertEquals(movie.overview, dataReceived.overview)
    }
}