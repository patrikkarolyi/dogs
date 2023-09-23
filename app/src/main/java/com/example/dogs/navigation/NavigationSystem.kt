package com.example.dogs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogs.ui.details.DetailsScreen
import com.example.dogs.ui.favorite.FavoriteScreen
import com.example.dogs.ui.list.ListScreen

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screen.ListScreen.route,
    ) {
        composable(route = Screen.ListScreen.route){
            ListScreen(
                navController = navController,
            )
        }

        composable(
            route = Screen.DetailsScreen.route + "/{breedId}/{title}",
            arguments = listOf(
                navArgument("breedId"){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("title"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )){entry ->
            DetailsScreen(
                navController = navController,
                breedId = entry.arguments?.getString("breedId") ?: "",
                title = entry.arguments?.getString("title") ?: "",
            )
        }

        composable(route = Screen.FavoriteScreen.route){
            FavoriteScreen(
                navController = navController,
            )
        }
    }
}