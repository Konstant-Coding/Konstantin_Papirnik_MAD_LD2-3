package com.example.ld2

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ld2.Composables.Screen
import com.example.ld2.screens.HomeScreen
import com.example.ld2.screens.FavoriteScreen
import com.example.ld2.screens.DetailsScreen

@Composable
fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Favorites.route
        ){
            FavoriteScreen(navController = navController)
        }
        composable(
            route= Screen.Detail.route +"/{movieId}",
            arguments = listOf(navArgument("movieId"){
                type = NavType.StringType
            })
        ){ backStackEntry ->
            DetailsScreen(navController, movieId = backStackEntry.arguments?.getString("movieId"))
        }
    }
}