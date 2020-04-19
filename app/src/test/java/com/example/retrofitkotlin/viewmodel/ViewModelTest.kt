package com.example.retrofitkotlin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelTest {
    lateinit var movieViewModel: MovieViewModel

    @Mock
    lateinit var movieRepository: MovieRepository
    private lateinit var postMovieEmptyList: List<TmdMovie>
    private val mockList: MutableList<TmdMovie> = mutableListOf()
    private val mockLiveData: MutableLiveData<List<TmdMovie>> = MutableLiveData<List<TmdMovie>>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        this.movieViewModel = MovieViewModel(this.movieRepository)
        mockData()
    }

    @Test
    fun fetchMovieFromRepositoryTest() {
        runBlocking {
            Mockito.`when`(movieRepository.getPopularMovies()).thenReturn(mockList)
            movieViewModel.fetchMovies()
            Assert.assertNotNull(movieViewModel.popularMoviesLiveData.value)
        }
    }

    private fun mockData() {
        postMovieEmptyList = emptyList()
        val movie = TmdMovie(
            1,
            2.00,
            "filme",
            "fdfddffd",
            "aaaaaa",
            "kkkkkkkk",
            "lllllllll"
        )
        mockList.add(movie)
        postMovieEmptyList = mockList.toList()
        mockLiveData.postValue(postMovieEmptyList)
    }
}