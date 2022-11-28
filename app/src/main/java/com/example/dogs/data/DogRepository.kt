package com.example.dogs.data

import javax.inject.Inject

class DogRepository @Inject constructor() {
    fun getAllBreeds(): List<String> {
        return listOf("Vizsla","Komondor","Kuvasz")
    }
}