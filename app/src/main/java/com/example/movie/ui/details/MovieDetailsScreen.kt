package com.example.movie.ui.details

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.movie.model.Constants
import com.example.movie.model.Constants.artworkUrl
import com.example.movie.model.Movie
import com.example.movie.model.placeholderImage
import com.example.movie.model.sampleMovieData
import com.example.movie.navigation.Actions
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.viewmodel.MovieDetailViewModel
import com.skydoves.landscapist.coil.CoilImage

private const val EMPTY_STRING = ""

@Composable
fun MovieDetailsScreen(
    movie: Movie,
    actions: Actions,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
        }
    }
    viewModel.getMovieCredits(movie.id)
    val requestState by viewModel.stateDetails.collectAsState()

    if (requestState.isLoading) {
        Loading()
    } else {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorBackground),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            BackdropView(movie, actions)
            Row {
                MovieInformation(movie, requestState.director)
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .width(130.dp)
                        .padding(end = 10.dp)
                ) {
                    ImageView(
                        idImage = movie.id,
                        description = movie.title,
                        ContentScale.Inside,
                        Constants.artworkDetailImagePoster(movie.posterImage ?: EMPTY_STRING)
                    )
                }
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
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White, modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun BackdropView(
    movie: Movie,
    actions: Actions
) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        ImageView(
            movie.id,
            movie.title,
            ContentScale.Crop,
            artworkUrl(movie.backDropImage ?: "")
        )
        Box(modifier = Modifier.padding(top = 30.dp)) {
            IconButton(onClick = { actions.navigateUp() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "backIcon",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun MovieInformation(movie: Movie, directorName: String) {
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
            text = "DIRECTED BY",
            color = Color.White,
            fontSize = 10.sp
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = directorName,
            color = Color.White,
            fontSize = 13.sp
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = movie.releaseDate.dropLast(6),
            color = Color.White,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun ImageView(
    idImage: Int,
    description: String,
    contentScale: ContentScale,
    imageUrl: String
) {
    CoilImage(
        imageModel = { imageUrl },
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
                    movie = sampleMovieData[0],
                    actions = actions
                )
            }
        }
    }
}