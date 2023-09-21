package com.example.dogs.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.ImageData
import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkHttpError
import com.example.dogs.data.network.model.NetworkIOError
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import com.example.dogs.data.network.model.NetworkUnavailable
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val dogAPI: DogAPI,
    private val  context: Context
) {

    suspend fun getAllBreeds(): NetworkResponse<AllBreedData> {
        return executeNetworkCall(context) {
            dogAPI.getAllBreeds()
        }
    }

    suspend fun getRandomUrlOfBreed(breed: String, subBreed: String): NetworkResponse<ImageData> {
        return executeNetworkCall(context) {
            if(subBreed.isBlank()){
                dogAPI.getRandomUrlOfBreed(breed)
            }else{
                dogAPI.getRandomUrlOfBreed(breed,subBreed)
            }
        }
    }

    suspend fun getAllUrlOfBreed(breed: String, subBreed: String): NetworkResponse<ImagesData> {
        return executeNetworkCall(context) {
            if(subBreed.isBlank()){
                dogAPI.getAllUrlOfBreed(breed)
            }else{
                dogAPI.getAllUrlOfBreed(breed,subBreed)
            }
        }
    }
}

suspend fun <T : Any> executeNetworkCall(context: Context, block: suspend () -> T): NetworkResponse<T> {
    if (!isInternetAvailable(context)) {
        return NetworkUnavailable
    }
    return try {
        val networkResult = block.invoke()
        NetworkResult(networkResult)
    } catch (e: IOException) {
        NetworkIOError
    } catch (e: HttpException) {
        NetworkHttpError(e.code())
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}