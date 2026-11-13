package com.example.movie.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.movie.model.Movie
import com.example.movie.theme.colorText

@Composable
fun ErrorRow(movies: LazyPagingItems<Movie>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { movies.retry() }) {
            Text(
                text = "Failed to load movies. Tap to retry",
                color = colorText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}