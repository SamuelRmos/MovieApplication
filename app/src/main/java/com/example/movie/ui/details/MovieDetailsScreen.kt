package com.example.movie.ui.details

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.DimenRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import com.example.movie.R
import com.example.movie.model.Cast
import com.example.movie.model.Constants
import com.example.movie.model.Constants.artworkUrl
import com.example.movie.model.Movie
import com.example.movie.model.placeholderImage
import com.example.movie.model.sampleMovieData
import com.example.movie.navigation.Actions
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.ui.state.DetailsState
import com.example.movie.viewmodel.MovieDetailViewModel
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val DARK_LUMINANCE_THRESHOLD = 0.5f

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
    LaunchedEffect(movie.id) {
        viewModel.getMovieCredits(movie.id)
    }
    val requestState by viewModel.stateDetails.collectAsState()

    MovieDetailsContent(
        movie = movie,
        actions = actions,
        requestState = requestState
    )
}

@Composable
fun MovieDetailsContent(
    movie: Movie,
    actions: Actions,
    requestState: DetailsState
) {
    val isInEditMode = LocalView.current.isInEditMode
    var backgroundColor by remember(movie.id) { mutableStateOf(colorBackground) }
    var textColor by remember(movie.id) { mutableStateOf(Color.White) }
    var backdropBitmap by remember(movie.id) { mutableStateOf<Bitmap?>(null) }
    var isPaletteReady by remember(movie.id) {
        mutableStateOf(isInEditMode || movie.backDropImage.isNullOrBlank())
    }

    LaunchedEffect(backdropBitmap) {
        val bitmap = backdropBitmap ?: return@LaunchedEffect
        val extractedColors = withContext(Dispatchers.Default) {
            extractDominantSwatchColors(bitmap)
        }
        extractedColors?.let { (dominantBackground, contrastingText) ->
            backgroundColor = dominantBackground
            textColor = contrastingText
        }
        isPaletteReady = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val padding10 = dimensionResource(R.dimen.dimen_10)
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(backgroundColor),
            verticalArrangement = Arrangement.spacedBy(padding10),
        ) {
            BackdropView(
                movie = movie,
                actions = actions,
                arrowBackColor = textColor,
                onBitmapLoaded = { backdropBitmap = it },
                onBitmapLoadFailed = { isPaletteReady = true }
            )
            if (!requestState.isLoading) {
                Row {
                    MovieInformation(
                        movie = movie,
                        directorName = requestState.director,
                        videoKey = requestState.key,
                        textColor = textColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.dimen_150))
                            .width(dimensionResource(R.dimen.dimen_130))
                            .padding(end = padding10)
                    ) {
                        ImageView(
                            idImage = movie.id,
                            description = movie.title,
                            contentScale = ContentScale.Inside,
                            imageUrl = Constants.artworkDetailImagePoster(movie.posterImage.orEmpty())
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(start = padding10, end = padding10),
                    text = movie.overview,
                    color = textColor,
                    fontSize = textSizeResource(R.dimen.text_size_15)
                )
                CastRow(
                    movieId = movie.id,
                    castList = requestState.cast,
                    textColor = textColor
                )
            }
        }

        if (requestState.isLoading || !isPaletteReady) {
            Loading()
        }
    }
}

internal fun extractDominantSwatchColors(bitmap: Bitmap): Pair<Color, Color>? {
    val softwareBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        bitmap
    } ?: return null

    val swatch = Palette.from(softwareBitmap).generate().dominantSwatch ?: return null
    val backgroundColor = Color(swatch.rgb)
    val textColor = if (backgroundColor.luminance() < DARK_LUMINANCE_THRESHOLD) {
        Color.White
    } else {
        Color.Black
    }
    return backgroundColor to textColor
}

@Composable
private fun CastRow(movieId: Int, castList: List<Cast>, textColor: Color) {
    val padding10 = dimensionResource(R.dimen.dimen_10)
    val padding4 = dimensionResource(R.dimen.dimen_4)
    val size70 = dimensionResource(R.dimen.dimen_70)
    Text(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.dimen_14),
            vertical = padding4
        ),
        text = stringResource(R.string.cast_section_title),
        color = textColor,
        fontSize = textSizeResource(R.dimen.text_size_16),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = padding10, end = padding10),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dimen_12))
    ) {
        castList.forEach { cast ->
            Column(
                modifier = Modifier.width(dimensionResource(R.dimen.dimen_80)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(size70)
                        .clip(CircleShape)
                ) {
                    ImageView(
                        idImage = movieId,
                        description = cast.name,
                        contentScale = ContentScale.Crop,
                        imageUrl = Constants.artworkDetailImagePoster(cast.imageProfile.orEmpty()),
                        modifier = Modifier
                            .size(size70)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(padding4))
                Text(
                    text = cast.name,
                    color = textColor,
                    fontSize = textSizeResource(R.dimen.text_size_12),
                    textAlign = TextAlign.Center
                )
            }
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
            color = Color.White,
            modifier = Modifier.size(dimensionResource(R.dimen.dimen_48))
        )
    }
}

@Composable
private fun BackdropView(
    movie: Movie,
    actions: Actions,
    arrowBackColor: Color,
    onBitmapLoaded: ((Bitmap) -> Unit)? = null,
    onBitmapLoadFailed: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .height(dimensionResource(R.dimen.dimen_200))
            .fillMaxWidth()
    ) {
        ImageView(
            idImage = movie.id,
            description = movie.title,
            contentScale = ContentScale.Crop,
            imageUrl = artworkUrl(movie.backDropImage.orEmpty()),
            onBitmapLoaded = onBitmapLoaded,
            onBitmapLoadFailed = onBitmapLoadFailed
        )
        Box(modifier = Modifier.padding(top = dimensionResource(R.dimen.dimen_30))) {
            IconButton(onClick = { actions.navigateUp() }) {
                Icon(
                    Filled.ArrowBack,
                    "backIcon",
                    tint = arrowBackColor
                )
            }
        }
    }
}

@Composable
private fun MovieInformation(
    movie: Movie,
    directorName: String,
    videoKey: String,
    textColor: Color
) {
    val context = LocalContext.current
    val padding10 = dimensionResource(R.dimen.dimen_10)
    val textSize13 = textSizeResource(R.dimen.text_size_13)
    Column(modifier = Modifier.width(dimensionResource(R.dimen.dimen_200))) {
        Text(
            modifier = Modifier.padding(start = padding10, bottom = padding10, end = padding10),
            text = movie.title,
            color = textColor,
            fontSize = textSizeResource(R.dimen.text_size_25),
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(start = padding10),
            text = "DIRECTED BY",
            color = textColor,
            fontSize = textSizeResource(R.dimen.text_size_10)
        )
        Text(
            modifier = Modifier.padding(start = padding10),
            text = directorName,
            color = textColor,
            fontSize = textSize13
        )
        Row {
            Text(
                modifier = Modifier.padding(start = padding10),
                text = movie.releaseDate.dropLast(6),
                color = textColor,
                fontSize = textSize13
            )
            Text(
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.dimen_100))
                    .clickable {
                        openYoutubeLink(videoKey, context)
                    },
                text = "Trailer",
                color = textColor,
                fontSize = textSizeResource(R.dimen.text_size_16),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun openYoutubeLink(youtubeID: String, context: Context) {
    val intentApp = Intent(
        Intent.ACTION_VIEW,
        "vnd.youtube:$youtubeID".toUri()
    )
    val intentBrowser = Intent(
        Intent.ACTION_VIEW,
        "http://www.youtube.com/watch?v=$youtubeID".toUri()
    )
    try {
        context.startActivity(intentApp)
    } catch (_: ActivityNotFoundException) {
        context.startActivity(intentBrowser)
    }
}

@Composable
private fun ImageView(
    idImage: Int,
    description: String,
    contentScale: ContentScale,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onBitmapLoaded: ((Bitmap) -> Unit)? = null,
    onBitmapLoadFailed: (() -> Unit)? = null
) {
    CoilImage(
        modifier = modifier,
        imageModel = { imageUrl },
        previewPlaceholder = placeholderImage(idImage),
        success = { imageState ->
            imageState.drawable?.let { drawable ->
                val bitmap = remember(drawable) { drawable.toBitmap() }
                LaunchedEffect(bitmap) {
                    onBitmapLoaded?.invoke(bitmap)
                }
                Image(
                    modifier = modifier,
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = description,
                    contentScale = contentScale
                )
            }
        },
        failure = {
            LaunchedEffect(Unit) {
                onBitmapLoadFailed?.invoke()
            }
        }
    )
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
                MovieDetailsContent(
                    movie = sampleMovieData[1],
                    actions = actions,
                    requestState = DetailsState(
                        isLoading = false,
                        director = "Director Name",
                        key = "videoKey",
                        cast = emptyList()
                    )
                )
            }
        }
    }
}
