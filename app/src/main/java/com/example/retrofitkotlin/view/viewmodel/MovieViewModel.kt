package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.LiveData
import com.example.retrofitkotlin.di.IoDispatcher
import com.example.retrofitkotlin.di.MainDispatcher
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.usecase.MovieUseCase
import com.example.retrofitkotlin.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

sealed class MovieViewAction {
    open class SuccessPopularMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessRatedMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessTodayMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessClassicMovie(val list: MovieResponse) : MovieViewAction()
    open class Loading(val loading: Boolean) : MovieViewAction()
    open class Error(val message: String) : MovieViewAction()
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, ioDispatcher) {

    private val _actionView by lazy { SingleLiveEvent<MovieViewAction>() }
    val actionView: LiveData<MovieViewAction>
        get() = _actionView

    fun fetchPopularMovies(isConnected: Boolean) {
        _actionView.value = MovieViewAction.Loading(true)
        mUiScope.launch {
            executePopularMovie(isConnected)
        }
    }

    fun fetchRatedMovies(isConnected: Boolean) {
        _actionView.value = MovieViewAction.Loading(true)
        mUiScope.launch {
            executeRatedMovie(isConnected)
        }
    }

    fun fetchTodayMovies(isConnected: Boolean) {
        _actionView.value = MovieViewAction.Loading(true)
        mUiScope.launch {
            executeTodayMovie(isConnected)
        }
    }

    fun fetchClassicMovies(isConnected: Boolean) {
        _actionView.value = MovieViewAction.Loading(true)
        mUiScope.launch {
            executeClassicMovie(isConnected)
        }
    }

    private suspend fun executePopularMovie(connected: Boolean) {
        mIoScope.async {
            return@async movieUseCase.executePopularMovies(connected)
        }.await().fold(::showError, ::showSuccessPopularMovie)
    }

    private suspend fun executeRatedMovie(connected: Boolean) {
        mIoScope.async {
            return@async movieUseCase.executeRatedMovies(connected)
        }.await().fold(::showError, ::showSuccessRatedMovie)
    }

    private suspend fun executeTodayMovie(connected: Boolean) {
        mIoScope.async {
            return@async movieUseCase.executeTodayMovies(connected)
        }.await().fold(::showError, ::showSuccessTodayMovie)
    }

    private suspend fun executeClassicMovie(connected: Boolean) {
        mIoScope.async {
            return@async movieUseCase.executeClassicMovies(connected)
        }.await().fold(::showError, ::showSuccessClassicMovie)
    }

    private fun showError(failure: String) {
        _actionView.value = MovieViewAction.Loading(false)
        _actionView.value = MovieViewAction.Error(failure)
    }

    private fun showSuccessPopularMovie(value: MovieResponse) {
        _actionView.value = MovieViewAction.Loading(false)
        _actionView.value = MovieViewAction.SuccessPopularMovie(value)
    }

    private fun showSuccessTodayMovie(value: MovieResponse) {
        _actionView.value = MovieViewAction.Loading(false)
        _actionView.value = MovieViewAction.SuccessTodayMovie(value)
    }

    private fun showSuccessClassicMovie(value: MovieResponse) {
        _actionView.value = MovieViewAction.Loading(false)
        _actionView.value = MovieViewAction.SuccessClassicMovie(value)
    }

    private fun showSuccessRatedMovie(value: MovieResponse) {
        _actionView.value = MovieViewAction.Loading(false)
        _actionView.value = MovieViewAction.SuccessRatedMovie(value)
    }

    // fun getListMovies() = movieRepository.getMoviePoster()
}