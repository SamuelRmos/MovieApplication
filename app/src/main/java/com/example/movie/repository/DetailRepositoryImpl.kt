package com.example.movie.repository

import androidx.annotation.WorkerThread
import com.example.commons.functional.Either
import com.example.commons.functional.Either.Error
import com.example.commons.functional.Either.Success
import com.example.movie.model.MovieCredits
import com.example.movie.network.MovieApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_MESSAGE = "Error while fetching movies credits"

@WorkerThread
class DetailRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : DetailRepository {
    override suspend fun getMovieCredits(id: Int) = safeApiCall { movieApi.getCreditsMovie(id) }

    @VisibleForTesting
    internal suspend fun safeApiCall(
        call: suspend () -> Response<MovieCredits>
    ) = flow {
        try {
            call().run {
                body()?.let {
                    if (isSuccessful) {
                        emit(Success(it))
                    } else {
                        emit(Error(ERROR_MESSAGE))
                    }
                } ?: emit(Error("Error response body is null"))
            }
        } catch (exception: Exception) {
            Timber.e(exception, exception.message)
            emit(Error(ERROR_MESSAGE))
        }
    }.flowOn(IO)
}