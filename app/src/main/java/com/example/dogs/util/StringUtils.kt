package com.example.dogs.util

import java.util.Locale

fun String.capFirst() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
