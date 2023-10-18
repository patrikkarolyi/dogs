package com.example.dogs.data.network.model

import com.example.dogs.R
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

object NetworkIOError : NetworkNoResult()

class NetworkHttpError(val errorCode: Int) : NetworkNoResult()

class NetworkResult<out T : Any>(val result: T) : NetworkResponse<T>()

fun NetworkResponse<*>.translateNetworkResponse(): NetworkErrorPresentationModel {
    return NetworkErrorPresentationModel(
        timestamp = System.currentTimeMillis(),
        errorCode = if (this is NetworkHttpError) this.errorCode.toString() else "",
        message = when (this) {
            NetworkIOError -> R.string.no_internet_connection
            is NetworkHttpError -> R.string.http_error
            else -> R.string.unknown_error
        },
    )
}