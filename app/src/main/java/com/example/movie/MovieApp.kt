package com.example.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.movie.navigation.Actions
import com.example.movie.navigation.NavGraph

@Composable
fun MovieApp() {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    NavGraph(navController, actions)
}