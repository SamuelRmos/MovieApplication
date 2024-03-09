package com.example.movie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.movie.model.Constants
import com.example.movie.model.Movie
import com.example.movie.model.placeholderImage
import com.example.movie.theme.colorBackground
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MovieDetails(movie: Movie, navController: NavHostController) {
    Scaffold(
        topBar = {
            CustomToolbarScreen(navController = navController, title = "CineBook", true)
        },
        containerColor = colorBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                Modifier
                    .height(280.dp)
                    .width(200.dp)) {
                CoilImage(
                    imageModel = { Constants.artworkUrl(movie.backDropImage) },
                    previewPlaceholder = placeholderImage(movie.id),
                    success = { imageState ->
                        imageState.drawable?.let {
                            Image(
                                bitmap = it.toBitmap().asImageBitmap(),
                                contentDescription = movie.title,
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                )
            }
        }
    }
}