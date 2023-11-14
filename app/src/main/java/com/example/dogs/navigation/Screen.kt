package com.example.dogs.navigation

sealed class Screen(val route : String) {
    data object ListScreen : Screen("list_screen")
    data object DetailsScreen : Screen("setting_screen")
    data object FavoriteScreen : Screen("favimg_screen")
    data object GameScreen : Screen("game_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}