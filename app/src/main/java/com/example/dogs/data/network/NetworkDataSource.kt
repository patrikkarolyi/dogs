package com.example.dogs.data.network

import com.example.dogs.data.network.model.AllBreedData
import com.example.dogs.data.network.model.ImagesData
import com.example.dogs.data.network.model.NetworkHttpError
import com.example.dogs.data.network.model.NetworkIOError
import com.example.dogs.data.network.model.NetworkResponse
import com.example.dogs.data.network.model.NetworkResult
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val dogAPI: DogAPI
) {

    suspend fun getAllBreeds(): NetworkResponse<AllBreedData> {
        return executeNetworkCall {
            dogAPI.getAllBreeds()
        }
    }

    suspend fun getAllUrlOfBreed(breed: String, subBreed: String): NetworkResponse<ImagesData> {
        return executeNetworkCall {
            if(subBreed.isBlank()){
                dogAPI.getAllUrlOfBreed(breed)
            }else{
                dogAPI.getAllUrlOfBreed(breed,subBreed)
            }
        }
    }
}

suspend fun <T : Any> executeNetworkCall(block: suspend () -> T): NetworkResponse<T> {
    return try {
        NetworkResult(block.invoke())
    } catch (e: IOException) {
        NetworkIOError
    } catch (e: HttpException) {
        NetworkHttpError(e.code())
    }
}