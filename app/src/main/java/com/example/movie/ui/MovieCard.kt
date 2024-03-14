package com.example.movie.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.movie.model.Constants.artworkUrl
import com.example.movie.model.Movie
import com.example.movie.model.SampleMovieData
import com.example.movie.model.placeholderImage
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.theme.colorPrimary
import com.example.movie.theme.colorText
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onMovieClick: (Movie) -> Unit
) {
    Surface(
        modifier = modifier.clickable { onMovieClick(movie) },
        color = colorPrimary
    ) {
        MovieCardContent(movie = movie)
    }
}

@Composable
fun MovieCardContent(modifier: Modifier = Modifier, movie: Movie) {
    Box(
        modifier
            .height(200.dp)
            .width(180.dp)) {
        CoilImage(
            imageModel = { artworkUrl(movie.posterImage) },
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

@Composable
fun MovieTitle(title: String?, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title ?: "",
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = colorText
    )
}

@Preview
@Composable
private fun MovieCardPreview() {
    MovieTheme {
        Surface(color = colorBackground) {
            Row(
                Modifier
                    .width(200.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[0]
                ) {}
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[1]
                ) {}
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[2]
                ) {}
            }
        }
    }
}