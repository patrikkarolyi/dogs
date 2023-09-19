package com.example.dogs.util

import com.example.dogs.data.disk.model.RoomBreedData
import java.util.Locale

fun String.capFirst() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun RoomBreedData.fullName() = "${this.subBreedName.capFirst()} ${this.breedName.capFirst()}".trim()
