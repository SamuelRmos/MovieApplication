package com.example.movie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.rememberNavController
import com.example.movie.model.Constants
import com.example.movie.model.Movie
import com.example.movie.model.SampleMovieData
import com.example.movie.model.placeholderImage
import com.example.movie.navigation.Actions
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.ui.components.CustomToolbarScreen
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MovieDetailsScreen(movie: Movie, actions: Actions) {
    Scaffold(
        topBar = {
            CustomToolbarScreen(
                actions = actions,
                title = "",
                colorBackground,
                true
            )
        },
        containerColor = colorBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ImageView(
                Modifier
                    .height(280.dp)
                    .fillMaxWidth(),
                movie.backDropImage ?: "",
                movie.id,
                movie.title,
                ContentScale.FillBounds
            )
            Row {
                MovieInformation(movie)
                Spacer(modifier = Modifier.weight(1f))
                ImageView(
                    Modifier
                        .height(150.dp)
                        .width(130.dp)
                        .padding(end = 10.dp),
                    image = movie.posterImage,
                    idImage = movie.id,
                    description = movie.title,
                    contentScale = ContentScale.Inside
                )
            }
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = movie.overview,
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun MovieInformation(movie: Movie) {
    Column(modifier = Modifier.width(200.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp, end = 10.dp),
            text = movie.title,
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = movie.releaseDate.dropLast(6),
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun ImageView(
    modifier: Modifier,
    image: String,
    idImage: Int,
    description: String,
    contentScale: ContentScale,
) {
    Box(modifier = modifier) {
        CoilImage(
            imageModel = { Constants.artworkUrl(image) },
            previewPlaceholder = placeholderImage(idImage),
            success = { imageState ->
                imageState.drawable?.let {
                    Image(
                        bitmap = it.toBitmap().asImageBitmap(),
                        contentDescription = description,
                        contentScale = contentScale
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun MovieDetailScreenPreview() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        Actions(navController)
    }
    MovieTheme {
        Surface(color = colorBackground) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                MovieDetailsScreen(
                    movie = SampleMovieData[0],
                    actions = actions
                )
            }
        }
    }
}