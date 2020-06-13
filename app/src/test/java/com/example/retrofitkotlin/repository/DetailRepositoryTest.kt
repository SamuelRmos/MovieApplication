package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.junit.MockitoJUnitRunner

@RunWith(JUnit4::class)
class DetailRepositoryTest {
    //region constants

    //end region constants

    //region helper fields

    private lateinit var sut: DetailRepository
    private val movieDao = mockk<MovieDao>()

    private val mId = 419704
    private val mTitle = "Ad Astra"
    private val mOverview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown."

    // end region helper fields
    @Before
    fun setup() {
    }

    @Test
    fun `detailRepo dataFromDb retrieveData`() {

        every { movieDao.getMovie(419704) } returns mockMovie()

        sut = DetailRepository(movieDao)
        val dataReceived = sut.getMovieById(419704)

        assertNotNull(dataReceived)
        assertEquals(dataReceived.id, mId)
        assertEquals(dataReceived.title, mTitle)
        assertEquals(dataReceived.overview, mOverview)
    }
    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}