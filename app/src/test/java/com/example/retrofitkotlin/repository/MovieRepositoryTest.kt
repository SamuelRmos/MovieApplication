package com.example.retrofitkotlin.repository

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    //region constants

    //end region constants

    //region helper fields

    private lateinit var sut: MovieRepository

    // end region helper fields

    @Before
    fun setup() {
        sut = MovieRepository()
    }

    fun `movieRepoTest `() {

    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}