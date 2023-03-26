package com.example.ld2.Composables

sealed class Screen(val route: String){
    object Home: Screen(route = "home_screen")
    object Favorites: Screen(route = "favorite_screen")
    object Detail: Screen(route = "details_screen")
}