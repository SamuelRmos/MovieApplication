package com.example.movie.repository

import com.example.movie.model.MovieResponse
import com.example.movie.ui.RequestState
import com.example.movie.ui.RequestState.Error
import com.example.movie.ui.RequestState.Loading
import com.example.movie.ui.RequestState.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber

private const val ERROR_MESSAGE = "Error while fetching movies data"

open class BaseRepository {
    suspend fun safeApiCall(
        call: suspend () -> Response<MovieResponse>
    ): Flow<RequestState> = flow {
        try {
            call().run {
                emit(Loading)
                delay(1000)
                body()?.let {
                    if (isSuccessful) emit(Success(it.results)) else emit(Error(ERROR_MESSAGE))
                } ?: emit(Error("Error response body is null"))
            }
        } catch (exception: Exception) {
            Timber.e(exception, exception.message)
            emit(Error(ERROR_MESSAGE))
        }
    }
}