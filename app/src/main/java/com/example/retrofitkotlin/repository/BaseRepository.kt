package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.functional.Either
import retrofit2.Response

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {

        val result = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is Either.Success -> data = result.data
            is Either.Error -> "$errorMessage & Exception - ${result.errorData}"
        }

        return data
    }

    private suspend fun <T: Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Either<String, T> {

        val response = call.invoke()
        if (response.isSuccessful) return Either.Success(response.body()!!)

        return Either.Error(
            "Error Occurred during getting safe Api result, " +
                    "Custom Error - $errorMessage"
        )
    }
}