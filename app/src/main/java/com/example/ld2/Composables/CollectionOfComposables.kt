package com.example.ld2.Composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ld2.CoreComponents.Movie
import com.example.ld2.CoreComponents.getMovies

@Composable
fun SimpleAppBar(title: String, navController: NavController){
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(50.dp),
                contentDescription = "",
                imageVector = Icons.Default.ArrowBack
            )
        }
    )
}

@Composable
fun MovieImage(data: String, description: String){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        contentDescription =  description,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ImageRow(images: List<String>, title: String){
    Divider(thickness = 2.dp, color = Color.Red)
    Text(text = title,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp)
    )
    LazyRow(Modifier.fillMaxWidth()){
        items(images) {
            Card(
                Modifier
                    .width(420.dp)
                    .height(220.dp)
                    .padding(5.dp)
            ) {
                MovieImage(data = it, description = "")
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onItemClick: (String) -> Unit = {}){
    val padding = 12.dp
    var showDetails by remember { mutableStateOf(false) }
    Card(
        Modifier
            .fillMaxWidth()
            .padding(padding)
            .clickable { onItemClick(movie.id) },
        shape = CutCornerShape(12.dp),
    ) {
        Column(
            Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                MovieImage(data = movie.images[0], description = "")
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.TopEnd
                ) {
                    ToggleIcon(
                        icon = Icons.Default.FavoriteBorder,
                        toggleIcon = Icons.Default.Favorite,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = ""){}
                }
            }
            Spacer(Modifier.size(padding))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.h5)
                ToggleIcon(
                    icon = Icons.Default.KeyboardArrowUp,
                    toggleIcon = Icons.Default.KeyboardArrowDown,
                    contentDescription = ""){
                    showDetails = !showDetails
                }
            }
            AnimatedVisibility(visible = showDetails){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(padding)) {
                    Text(style = MaterialTheme.typography.body1,
                        text = "Director: ${movie.director} \n" +
                                "Released: ${movie.year} \n" +
                                "Genre: ${movie.genre} \n" +
                                "Actors: ${movie.actors} \n" +
                                "Rating: ${movie.rating}")
                    Spacer(Modifier.size(padding))
                    Divider(thickness = 2.dp, color = Color.Red)
                    Spacer(Modifier.size(padding))
                    Text(text = "Plot: ${movie.plot}", Modifier.padding(padding))
                }
            }
        }
    }
}



@Composable
fun ToggleIcon(icon: ImageVector,
               toggleIcon: ImageVector,
               tint: Color = Color.Black,
               contentDescription: String = "",
               onIconClick: () -> Unit = {}){
    var showIcon = icon
    var clickIcon by remember { mutableStateOf(false) }
    if (clickIcon){ showIcon = toggleIcon }
    Icon(
        modifier = Modifier
            .clickable {
                clickIcon = !clickIcon
                onIconClick()
            }
            .size(30.dp),
        contentDescription = contentDescription,
        tint = tint,
        imageVector = showIcon
    )
}

@Composable
fun MovieList(navController: NavController = rememberNavController(), movies: List<Movie> = getMovies()){
    var expandedMenu by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        TopAppBar(
            elevation = 5.dp,
            title = { Text(text = "Movies") },
            actions = {
                Icon(
                    modifier = Modifier
                        .clickable { expandedMenu = !expandedMenu }
                        .size(30.dp)
                        .padding(5.dp),
                    contentDescription = "Additional Options",
                    imageVector = Icons.Default.MoreVert
                )
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false}) {
                    DropdownMenuItem(onClick = {
                        navController.navigate(Screen.Favorites.route)
                    }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Favorites",
                            imageVector = Icons.Default.Favorite
                        )
                        Spacer(Modifier.size(15.dp))
                        Text(text = "Favorites")
                    }
                }
            }
        )
        LazyColumn (userScrollEnabled = true) {
            items(movies) { movies ->
                MovieRow(movies){ movieId ->
                    navController.navigate(Screen.Detail.route + "/$movieId")
                }
            }
        }
    }
}
@Composable
fun MovieItem( //creating the movie items that are then passed to the movielist function
    movie: Movie
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    var isLiked by remember { //to keep the movies liked
        mutableStateOf(Color.Red)
    }
    var isOpened by remember { //to remember if movie details are expanded
        mutableStateOf(Icons.Rounded.KeyboardArrowDown)
    }
    //defining the shape of the movie item in form of cards for each movie
    Card(shape = CutCornerShape(20.dp), modifier = Modifier.padding(vertical = 15.dp)) {
        Column {
            Box(modifier = Modifier.height(150.dp)) {
                val painter = rememberAsyncImagePainter(model = movie.images[1]) //using coil to load images of movies
                Image(

                    contentDescription = "img desc",
                    painter = painter,

                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription =  "icon desc",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .clickable {
                            isLiked = if (isLiked == Color.Red) {
                                Color.Black
                            } else {
                                Color.Red
                            }
                        },
                    tint = isLiked,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .height(30.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = movie.title, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Icon(
                    imageVector = isOpened,
                    contentDescription = "img desc",
                    modifier = Modifier.clickable { //show details or not
                        if (isVisible) {
                            isOpened = Icons.Rounded.KeyboardArrowDown
                            isVisible = false
                        } else {
                            isOpened = Icons.Rounded.KeyboardArrowUp
                            isVisible = true
                        }
                    }
                )
            }
            AnimatedVisibility(visible = isVisible) {
                Column(Modifier.padding(20.dp)) {
                    Text(text = "Genre: ${movie.genre}")
                    Text(text = "Director: ${movie.director}")

                    Text(text = "Release: ${movie.year}")
                    Text(text = "Rating: ${movie.rating}" )
                    Text(text = "Actors: ${movie.actors}")

                    Divider(startIndent = 0.dp, thickness = 2.dp, color = Color.Red)

                    Spacer(modifier = Modifier.height(12.dp))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Plot :")
                    Text(text = movie.plot)
                }

            }
        }
    }
}
