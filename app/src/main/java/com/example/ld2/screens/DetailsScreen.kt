package com.example.ld2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ld2.CoreComponents.getMovies
import com.example.ld2.CoreComponents.Movie
import com.example.ld2.Composables.ImageRow
import com.example.ld2.Composables.MovieRow
import com.example.ld2.Composables.SimpleAppBar

@Composable
fun DetailsScreen(navController: NavController, movieId: String?){
    val movie: Movie = getMovies()[getMovies().indexOfFirst { it.id == movieId }]
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            SimpleAppBar(movie.title, navController)
            MovieRow(movie){}
            ImageRow(movie.images, "Img" )
        }
    }
}