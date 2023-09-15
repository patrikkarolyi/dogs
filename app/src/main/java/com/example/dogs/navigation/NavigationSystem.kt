package com.example.dogs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dogs.ui.detail.DetailScreen
import com.example.dogs.ui.detail.DetailViewModel
import com.example.dogs.ui.fav_dog.FavDogScreen
import com.example.dogs.ui.fav_dog.FavDogViewModel
import com.example.dogs.ui.fav_img.FavImgScreen
import com.example.dogs.ui.fav_img.FavImgViewModel
import com.example.dogs.ui.list.ListScreen
import com.example.dogs.ui.list.ListViewModel

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()
    val listViewModel: ListViewModel = viewModel()
    val detailViewModel: DetailViewModel = viewModel()
    val favDogViewModel: FavDogViewModel = viewModel()
    val favImgViewModel: FavImgViewModel = viewModel()


    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = Screen.ListScreen.route,
    ) {
        composable(route = Screen.ListScreen.route){
            ListScreen(
                navController = navController,
                viewModel = listViewModel
            )
        }

        composable(
            route = Screen.DetailScreen.route + "/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )){entry ->
            DetailScreen(
                navController = navController,
                viewModel = detailViewModel,
                breedId = entry.arguments?.getString("name") ?: ""
            )
        }

        composable(route = Screen.FavDogScreen.route){
            FavDogScreen(
                navController = navController,
                viewModel = favDogViewModel
            )
        }

        composable(route = Screen.FavImgScreen.route){
            FavImgScreen(
                navController = navController,
                viewModel = favImgViewModel
            )
        }
    }
}