package com.example.movie.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
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
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
        navigationIcon = {
            if (isBack) {
                IconButton(onClick = { actions.navigateUp() }) {
                    Icon(
                        Filled.ArrowBack,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSurface(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable () -> Unit,
) {
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    val appBarContainerColor by animateColorAsState(
        targetValue = lerp(
            colors.containerColor,
            colors.scrolledContainerColor,
            FastOutLinearInEasing.transform(fraction),
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "TopBarSurfaceContainerColorAnimation",
    )
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = appBarContainerColor,
        content = content,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit),
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    onQueryChange(query)
                },
                onSearch = onSearch,
                expanded = isSearchActive,
                onExpandedChange = activeChanged,
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
        expanded = isSearchActive,
        onExpandedChange = activeChanged,
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        )
    ) {
        // Search suggestions or results
    }
}