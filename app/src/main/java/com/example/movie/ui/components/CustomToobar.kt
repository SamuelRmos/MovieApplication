package com.example.movie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomToolbarScreen(
    actions: Actions,
    title: String,
    backgroundColor: Color,
    isBack: Boolean = false,
    windowsInsets: WindowInsets = TopAppBarDefaults.windowInsets
) {
    val scaffoldState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDrawerOpen = remember {
        mutableStateOf(false)
    }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp
            )
        },
        modifier = Modifier.background(colorPrimary),
        navigationIcon = {
            if (isBack) {
                IconButton(onClick = { actions.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "backIcon",
                        tint = Color.White
                    )
                }
            } else {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.open()
                    }
                }) {
                   // Icon(Icons.Filled.Menu, "backIcon", tint = Color.White)
                }
            }
        },
        windowInsets = windowsInsets
    )
}