//package com.example.retrofitkotlin.view.viewmodel
//
//import com.example.retrofitkotlin.persistence.MovieDao
//import com.example.retrofitkotlin.repository.DetailRepository
//import com.example.retrofitkotlin.utils.MockTestUtil.mockMovie
//import junit.framework.Assert.assertNotNull
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.MockitoJUnitRunner
//
//@RunWith(MockitoJUnitRunner::class)
//class ViewModelTest {
//
//    private lateinit var sut: DetailRepository
//
//    @Mock
//    lateinit var movieDao: MovieDao
//
//    @Before
//    fun init() {
//    }
//
//    @Test
//    fun fetchMovieFromRepositoryTest() {
//
//        `when`(movieDao.getMovie(419704)).thenReturn(mockMovie())
//        sut = DetailRepository(movieDao)
//
//        val data = sut.getMovieById(419704)
//
//        assertNotNull(data)
//    }
//
//}