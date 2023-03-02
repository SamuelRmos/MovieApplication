package com.example.retrofitkotlin.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.example.retrofitkotlin.MovieApp
import com.example.retrofitkotlin.theme.MovieTheme
import com.example.retrofitkotlin.view.viewmodel.RootViewModel
import com.skydoves.landscapist.coil.LocalCoilImageLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : ComponentActivity() {

    private val viewModel: RootViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CompositionLocalProvider(LocalCoilImageLoader provides viewModel.imageLoader) {
                MovieTheme {
                    MovieApp()
                }
            }
        }
    }
}
