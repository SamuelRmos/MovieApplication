package com.example.movie.ui.search

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.R
import com.example.movie.model.Constants
import com.example.movie.model.Movie
import com.example.movie.model.SearchMovieItem
import com.example.movie.model.placeholderImage
import com.example.movie.model.sampleMovieData
import com.example.movie.navigation.Actions
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.ui.state.SearchState
import com.example.movie.viewmodel.SearchViewModel
import com.skydoves.landscapist.coil.CoilImage

private const val YEAR_CHAR_COUNT = 4

@Composable
fun SearchScreen(
    actions: Actions,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
        }
    }

    val searchState by viewModel.searchState.collectAsState()

    SearchScreenContent(
        searchState = searchState,
        onQueryChange = viewModel::onQueryChange,
        onBackClick = actions.navigateUp,
        onMovieClick = actions.goToMovieDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    searchState: SearchState,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val isInEditMode = LocalView.current.isInEditMode
    LaunchedEffect(Unit) {
        if (!isInEditMode) {
            focusRequester.requestFocus()
        }
    }

    val padding6 = dimensionResource(R.dimen.dimen_6)
    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorBackground)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = padding6,
                    end = padding6,
                    top = dimensionResource(R.dimen.dimen_2),
                    bottom = padding6
                ),
            shape = SearchBarDefaults.inputFieldShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            SearchBarDefaults.InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                query = searchState.query,
                onQueryChange = onQueryChange,
                onSearch = onQueryChange,
                expanded = false,
                onExpandedChange = {},
                placeholder = {
                    Text(text = stringResource(R.string.search_placeholder))
                },
                leadingIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Filled.ArrowBack,
                            contentDescription = "backIcon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                trailingIcon = {
                    if (searchState.query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "clearSearch",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }

        if (searchState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(dimensionResource(R.dimen.dimen_48))
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = dimensionResource(R.dimen.dimen_14))
            ) {
                items(
                    items = searchState.results,
                    key = { it.movie.id }
                ) { item ->
                    SearchResultRow(
                        item = item,
                        onMovieClick = onMovieClick
                    )
                    HorizontalDivider(
                        thickness = dimensionResource(R.dimen.dimen_1),
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    item: SearchMovieItem,
    onMovieClick: (Movie) -> Unit
) {
    val padding12 = dimensionResource(R.dimen.dimen_12)
    val padding10 = dimensionResource(R.dimen.dimen_10)
    val posterWidth = dimensionResource(R.dimen.dimen_50)
    val posterHeight = dimensionResource(R.dimen.dimen_75)
    val cornerRadius = dimensionResource(R.dimen.dimen_6)
    val titleFontSize = textSizeResource(R.dimen.text_size_16)
    val metaFontSize = textSizeResource(R.dimen.text_size_14)
    val directedByPrefix = stringResource(R.string.directed_by_prefix)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick(item.movie) }
            .padding(horizontal = padding12, vertical = padding10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val posterModifier = Modifier
            .width(posterWidth)
            .height(posterHeight)
            .clip(RoundedCornerShape(cornerRadius))

        Box(modifier = posterModifier) {
            CoilImage(
                modifier = posterModifier,
                imageModel = {
                    Constants.artworkDetailImagePoster(item.movie.posterImage.orEmpty())
                },
                previewPlaceholder = placeholderImage(item.movie.id),
                success = { imageState ->
                    imageState.drawable?.let { drawable ->
                        val bitmap = remember(drawable) { drawable.toBitmap() }
                        Image(
                            modifier = posterModifier,
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = item.movie.title,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(padding12))

        val year = item.movie.releaseDate.take(YEAR_CHAR_COUNT)
        val annotatedText = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(item.movie.title)
            }
            if (year.isNotBlank()) {
                withStyle(
                    SpanStyle(
                        color = Color.LightGray,
                        fontSize = metaFontSize,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(" $year")
                }
            }
            if (item.director.isNotBlank()) {
                withStyle(
                    SpanStyle(
                        color = Color.LightGray,
                        fontSize = metaFontSize,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append("$directedByPrefix ")
                }
                withStyle(
                    SpanStyle(
                        color = Color.LightGray,
                        fontSize = metaFontSize,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(item.director)
                }
            }
        }

        Text(text = annotatedText)
    }
}

@Composable
private fun dimensionResource(@DimenRes id: Int): Dp {
    val isInEditMode = LocalInspectionMode.current
    return if (isInEditMode) {
        when (id) {
            R.dimen.dimen_1 -> 1.dp
            R.dimen.dimen_4 -> 4.dp
            R.dimen.dimen_6 -> 6.dp
            R.dimen.dimen_10 -> 10.dp
            R.dimen.dimen_12 -> 12.dp
            R.dimen.dimen_14 -> 14.dp
            R.dimen.dimen_30 -> 30.dp
            R.dimen.dimen_48 -> 48.dp
            R.dimen.dimen_50 -> 50.dp
            R.dimen.dimen_70 -> 70.dp
            R.dimen.dimen_75 -> 75.dp
            R.dimen.dimen_80 -> 80.dp
            R.dimen.dimen_100 -> 100.dp
            R.dimen.dimen_130 -> 130.dp
            R.dimen.dimen_150 -> 150.dp
            R.dimen.dimen_200 -> 200.dp
            else -> 0.dp
        }
    } else {
        androidx.compose.ui.res.dimensionResource(id)
    }
}

@Composable
private fun textSizeResource(@DimenRes id: Int): TextUnit {
    val isInEditMode = LocalInspectionMode.current
    return if (isInEditMode) {
        when (id) {
            R.dimen.text_size_10 -> 10.sp
            R.dimen.text_size_12 -> 12.sp
            R.dimen.text_size_13 -> 13.sp
            R.dimen.text_size_14 -> 14.sp
            R.dimen.text_size_15 -> 15.sp
            R.dimen.text_size_16 -> 16.sp
            R.dimen.text_size_25 -> 25.sp
            else -> 16.sp
        }
    } else {
        with(LocalDensity.current) { dimensionResource(id).toSp() }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    MovieTheme {
        Surface(color = colorBackground) {
            SearchScreenContent(
                searchState = SearchState(
                    query = "Run",
                    results = listOf(
                        SearchMovieItem(
                            movie = sampleMovieData[0].copy(
                                title = "Run",
                                releaseDate = "2020-11-20"
                            ),
                            director = "Aneesh Chaganty"
                        ),
                        SearchMovieItem(
                            movie = sampleMovieData[1].copy(
                                title = "Run",
                                releaseDate = "1991-02-01"
                            ),
                            director = "Geoff Burrowes"
                        )
                    ),
                    isLoading = false
                ),
                onQueryChange = {},
                onBackClick = {},
                onMovieClick = {}
            )
        }
    }
}

