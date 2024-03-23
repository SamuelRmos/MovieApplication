package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commons.functional.Either
import com.example.commons.functional.onFailure
import com.example.commons.functional.onSuccess
import com.example.movie.model.MovieCredits
import com.example.movie.repository.DetailRepository
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {
    var requestState: MutableState<DetailRequestState> = mutableStateOf(Loading)
    private set

    fun getMovieCredits(id: Int) {
        executeCall {
            detailRepository.getMovieCredits(id)
        }
    }

    @VisibleForTesting
    internal fun executeCall(call: suspend () -> Flow<Either<String, MovieCredits>>) {
        viewModelScope.launch(IO) {
            call().collect {
               it.onSuccess { movieCredits ->
                   extractMovieData(movieCredits)
               }.onFailure { message ->
                   requestState.value = Error(message)
               }
            }
        }
    }

    private fun extractMovieData(movieCredits: MovieCredits) {
        val crew = movieCredits.crew.firstOrNull { it.job == "Director" }
        requestState.value = Success(crew?.name ?: "")
    }
}