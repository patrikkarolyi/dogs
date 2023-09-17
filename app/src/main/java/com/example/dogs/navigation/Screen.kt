package com.example.dogs.navigation

sealed class Screen(val route : String) {
    data object ListScreen : Screen("list_screen")
    data object DetailScreen : Screen("setting_screen")
    data object FavImgScreen : Screen("favimg_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}