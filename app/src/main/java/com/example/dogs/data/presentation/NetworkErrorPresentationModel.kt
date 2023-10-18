package com.example.dogs.data.presentation

import android.content.Context

data class NetworkErrorPresentationModel(
    val message: Int,
    val errorCode: String = "",
    val timestamp: Long
) {
    fun getStringMessage(context: Context): String {
        return StringBuilder(context.getString(message))
            .apply { if (errorCode.isNotBlank()) append(errorCode) }
            .toString()
    }
}