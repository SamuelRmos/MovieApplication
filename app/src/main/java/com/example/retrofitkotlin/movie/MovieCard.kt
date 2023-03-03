package com.example.retrofitkotlin.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.retrofitkotlin.model.Constants.artworkUrl
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.SampleMovieData
import com.example.retrofitkotlin.model.placeholderImage
import com.example.retrofitkotlin.theme.MovieTheme
import com.example.retrofitkotlin.theme.colorBackground
import com.example.retrofitkotlin.theme.colorPrimary
import com.example.retrofitkotlin.theme.colorText
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    Surface(
        modifier = modifier,
        color = colorPrimary,
        shape = RoundedCornerShape(10.dp)
    ) {
        MovieCardContent(movie = movie)
    }
}

@Composable
fun MovieCardContent(modifier: Modifier = Modifier, movie: Movie) {
    Box(modifier.height(280.dp).width(200.dp)) {
        CoilImage(
            imageModel = { artworkUrl(movie.poster_path) },
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
                Modifier.width(200.dp).fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[0]
                )
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[1]
                )
                MovieCard(
                    modifier = Modifier.fillMaxWidth(),
                    movie = SampleMovieData[2]
                )
            }
        }
    }
}