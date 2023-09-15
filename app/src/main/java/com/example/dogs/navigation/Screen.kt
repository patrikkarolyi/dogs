package com.example.dogs.navigation

sealed class Screen(val route : String) {
    object ListScreen : Screen("list_screen")
    object DetailScreen : Screen("setting_screen")
    object FavDogScreen : Screen("favdog_screen")
    object FavImgScreen : Screen("favimg_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}