@file:Suppress("DEPRECATION")

package com.example.retrofitkotlin.view.viewmodel

import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
import com.example.retrofitkotlin.viewmodel.MovieDetailViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieDetailViewModelTest {
    //region constants

    //end region constants

    //region helper fields

    private lateinit var sut: MovieDetailViewModel
    private val mDetailRepository = mockk<DetailRepositoryImpl>(relaxed = true)

    private val mId = 419704
    private val mTitle = "Ad Astra"
    private val mOverview =
        "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown."

    // end region helper fields

    @Before
    fun setup() {
    }

    @Test
    fun `movieDetailViewModel fetchData`() {

        every { mDetailRepository.getMovieById(419704) } returns mockMovie()
        sut = MovieDetailViewModel(mDetailRepository)

        val dataRetrieved = sut.getMovie(419704)

        assertNotNull(dataRetrieved)
        assertEquals(dataRetrieved.id, mId)
        assertEquals(dataRetrieved.title, mTitle)
        assertEquals(dataRetrieved.overview, mOverview)
    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}