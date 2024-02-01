package com.example.movie.repository

import com.example.commons.functional.Either
import com.example.commons.functional.Either.Error
import com.example.commons.functional.Either.Success
import retrofit2.Response

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Either<String, T> {
        call().run {
            return body()?.let {
                if (isSuccessful) Success(it) else Error("Error fetching movies data")
            } ?: Error("Error response body is null")
        }
    }
}