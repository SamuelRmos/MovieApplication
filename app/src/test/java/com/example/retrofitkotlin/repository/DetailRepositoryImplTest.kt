package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DetailRepositoryImplTest {
    private lateinit var sut: DetailRepositoryImpl
    private val movieDao = mockk<MovieDao>()

    @Before
    fun setup() {
        sut = DetailRepositoryImpl(movieDao)
    }

    @Test
    fun `getMovieById return data from database when Id passed is salved`() {
        val movie = mockMovie()
        val id = 419704
        every { movieDao.getMovie(id) } returns movie

        val dataReceived = sut.getMovieById(id)

        verify { movieDao.getMovie(id) }
        assertNotNull(dataReceived)
        assertEquals(movie.id, dataReceived.id)
        assertEquals(movie.title, dataReceived.title)
        assertEquals(movie.overview, dataReceived.overview)
    }
}