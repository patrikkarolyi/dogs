package com.example.dogs.data.network.model

import com.example.dogs.data.presentation.NetworkErrorPresentationModel

/**
 *                           NetworkResult<T>
 *                         /
 *                       /
 *  NetworkResponse<T>
 *                       \                   NetworkUnavailable
 *                        \                /
 *                         NetworkNoResult - NetworkIOError
 *                                         \
 *                                          NetworkHttpError
 */
sealed class NetworkResponse<out T : Any>

sealed class NetworkNoResult : NetworkResponse<Nothing>()

object NetworkUnavailable : NetworkNoResult()

object NetworkIOError : NetworkNoResult()

class NetworkHttpError(val errorCode: Int) : NetworkNoResult()

class NetworkResult<out T : Any>(val result: T) : NetworkResponse<T>()

fun NetworkResponse<*>.translateNetworkResponse(): NetworkErrorPresentationModel {
    return NetworkErrorPresentationModel(
        timestamp = System.currentTimeMillis(),
        message = when (this) {
            NetworkIOError -> "No internet connection!"
            is NetworkHttpError -> "HTTP error: ${this.errorCode}"
            else -> this.toString()
        },
    )
}