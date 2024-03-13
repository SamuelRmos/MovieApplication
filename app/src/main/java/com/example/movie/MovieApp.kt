package com.example.movie

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.movie.navigation.NavGraph

@Composable
fun MovieApp() {
    NavGraph(navController = rememberNavController())
}