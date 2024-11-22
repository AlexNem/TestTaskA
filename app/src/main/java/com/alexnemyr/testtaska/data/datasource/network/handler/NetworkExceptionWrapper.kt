package com.alexnemyr.testtaska.data.datasource.network.handler

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed interface Result<out T: Any> {
    data class Success<out T: Any>(val data: T) : Result<T>
    data class SuccessWithMessage<out T: Any>(val data: T, val message: MessageType) : Result<T>
    data class Error<out T: Any>(val message: String?) : Result<T>
}

suspend inline fun <reified T: Any> HttpResponse.toResult(): Result<T> {
    return when (status.value) {
        200 -> Result.Success(body())
        400 -> Result.Error(NetworkException("Check your credentials and try again!").message)
        401 -> Result.Error(NetworkException("Authorization Failed! Try Logging In again.").message)
        500, 503 -> Result.Error(NetworkException("Server Disruption! We are on fixing it.").message)
        504 -> Result.Error(NetworkException("Too much load at this time, try again later!").message)
        else -> Result.Error(NetworkException("Something went wrong! Please try again or contact support.").message)
    }
}

sealed class MessageType {
    object NO_INTERNET_CONNECTION: MessageType()
}

class NetworkException(message: String) : Exception(message)