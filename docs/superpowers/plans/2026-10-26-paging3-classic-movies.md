# Paging 3 Migration for ClassicMoviesViewModel Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `ClassicMoviesViewModel` and `ClassicMovieListRow` to use AndroidX Paging 3, introducing a reusable `MoviePagingSource` that fixes race conditions, synchronization flaws, and UI state fragmentation.

**Architecture:** Add Paging 3 dependencies to Gradle version catalogs. Introduce a generic `MoviePagingSource` that handles pagination window boundaries and max item caps (`MOVIES_LIMIT = 300`). Expose `Flow<PagingData<Movie>>` from `MovieRepository` and cache it in `ClassicMoviesViewModel`. Migrate `ClassicMovieListRow` to `LazyPagingItems<Movie>`, removing `InfiniteListHandler` and using native Paging 3 pull-to-refresh and append states.

**Tech Stack:** Kotlin 2, Android SDK 34, AndroidX Paging 3 (`paging-runtime-ktx`, `paging-compose`, `paging-testing`), Jetpack Compose, Coroutines Flow, Hilt, MockK, JUnit 4.

**Spec:** [docs/superpowers/specs/2026-10-26-paging3-classic-movies-design.md](file:///c:/Users/Public/Documents/Projects/docs/superpowers/specs/2026-10-26-paging3-classic-movies-design.md)

## Global Constraints

- Android SDK: CompileSdk 34, MinSdk 28.
- Upper boundary limit: `MOVIES_LIMIT = 300`.
- Page size: 20 items per page (matching TMDB API default).
- Dependency management: Centralized in `gradle/libs.versions.toml`.
- UI compatibility: Maintain existing Compose layout, spacing, movie cards, and navigation actions.

## Review Focus

1. **Initial load empty list:** When page 1 returns 0 movies, `MoviePagingSource` returns `LoadResult.Page` with `nextKey = null`, and UI shows an empty row without crashing.
2. **Hit maximum limit:** When page 15 completes (reaching 300 movies), `nextKey` is set to `null` so no further pages are requested.
3. **Network error resilience:** When `MovieApi` throws an IOException or returns non-200 HTTP status, `LoadResult.Error` is returned allowing retry.
4. **Pull-to-refresh reset:** Calling `movies.refresh()` in Compose reloads from page 1 without list duplication.
5. **Rapid fling / scroll concurrency:** Compose `collectAsLazyPagingItems()` handles automatic windowing without launching duplicate parallel requests for the same page.

---

### Task 1: Add AndroidX Paging 3 Dependencies

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

**Interfaces:**
- Produces: `libs.paging.runtime`, `libs.paging.compose`, `libs.paging.testing` available to Gradle build scripts.

- [ ] **Step 1: Update `gradle/libs.versions.toml` with Paging 3 libraries**

Add under `[versions]`:
```toml
pagingVersion = "3.3.0"
```
Add under `[libraries]`:
```toml
paging-runtime = { group = "androidx.paging", name = "paging-runtime-ktx", version.ref = "pagingVersion" }
paging-compose = { group = "androidx.paging", name = "paging-compose", version.ref = "pagingVersion" }
paging-testing = { group = "androidx.paging", name = "paging-testing", version.ref = "pagingVersion" }
```

- [ ] **Step 2: Update `app/build.gradle.kts` with Paging dependencies**

Add to `dependencies` block:
```kotlin
implementation(libs.paging.runtime)
implementation(libs.paging.compose)
testImplementation(libs.paging.testing)
```

- [ ] **Step 3: Verify Gradle builds successfully**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat :app:dependencies --configuration debugCompileClasspath
```
Expected: BUILD SUCCESSFUL with `androidx.paging:paging-runtime-ktx` resolved.

- [ ] **Step 4: Commit changes**

```bash
git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "build: add AndroidX Paging 3 dependencies"
```

---

### Task 2: Create and Test `MoviePagingSource`

**Files:**
- Create: `app/src/main/java/com/example/movie/data/paging/MoviePagingSource.kt`
- Create: `app/src/test/java/com/example/movie/data/paging/MoviePagingSourceTest.kt`

**Interfaces:**
- Consumes: `MovieApi` / `Response<MovieResponse>`
- Produces: `MoviePagingSource(maxLimit: Int = 300, fetchMovies: suspend (page: Int) -> Response<MovieResponse>) : PagingSource<Int, Movie>()`

- [ ] **Step 1: Write the failing unit tests for `MoviePagingSourceTest`**

Cover:
1. `load_initialPage_returnsSuccessWithNextKey`: Page 1 with 20 items returns `LoadResult.Page` with `prevKey = null`, `nextKey = 2`.
2. `load_reachingMaxLimit_returnsNextKeyNull`: When `(page * params.loadSize) >= maxLimit`, `nextKey` is `null`.
3. `load_emptyResults_returnsNextKeyNull`: When API returns empty results list, `nextKey` is `null`.
4. `load_httpError_returnsLoadResultError`: When `response.isSuccessful` is false, returns `LoadResult.Error`.
5. `load_exception_returnsLoadResultError`: When network throws `IOException`, returns `LoadResult.Error`.

- [ ] **Step 2: Run test to verify it fails**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat :app:testDebugUnitTest --tests "com.example.movie.data.paging.MoviePagingSourceTest"
```
Expected: FAIL (class `MoviePagingSource` does not exist yet).

- [ ] **Step 3: Implement `MoviePagingSource`**

Create `app/src/main/java/com/example/movie/data/paging/MoviePagingSource.kt`:
```kotlin
package com.example.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import retrofit2.Response

const val DEFAULT_MOVIES_LIMIT = 300

class MoviePagingSource(
    private val maxLimit: Int = DEFAULT_MOVIES_LIMIT,
    private val fetchMovies: suspend (page: Int) -> Response<MovieResponse>
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = fetchMovies(page)
            if (response.isSuccessful && response.body() != null) {
                val movies = response.body()!!.results
                val nextKey = if (movies.isEmpty() || (page * params.loadSize) >= maxLimit) {
                    null
                } else {
                    page + 1
                }
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Error while fetching movies data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
```

- [ ] **Step 4: Run unit tests to verify they pass**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat :app:testDebugUnitTest --tests "com.example.movie.data.paging.MoviePagingSourceTest"
```
Expected: PASS (all 5 test cases passing).

- [ ] **Step 5: Commit changes**

```bash
git add app/src/main/java/com/example/movie/data/paging/MoviePagingSource.kt app/src/test/java/com/example/movie/data/paging/MoviePagingSourceTest.kt
git commit -m "feat: implement and test MoviePagingSource"
```

---

### Task 3: Expose Paging in `MovieRepository`

**Files:**
- Modify: `app/src/main/java/com/example/movie/repository/MovieRepository.kt`
- Modify: `app/src/main/java/com/example/movie/repository/MovieRepositoryImpl.kt`

**Interfaces:**
- Consumes: `MoviePagingSource`, `MovieApi`
- Produces: `MovieRepository.getClassicMoviesPaging(): Flow<PagingData<Movie>>`

- [ ] **Step 1: Add `getClassicMoviesPaging` to `MovieRepository` interface**

In `MovieRepository.kt`:
```kotlin
fun getClassicMoviesPaging(): Flow<PagingData<Movie>>
```

- [ ] **Step 2: Implement `getClassicMoviesPaging` in `MovieRepositoryImpl`**

In `MovieRepositoryImpl.kt`:
```kotlin
override fun getClassicMoviesPaging(): Flow<PagingData<Movie>> = Pager(
    config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
    ),
    pagingSourceFactory = {
        MoviePagingSource { page -> movieApi.getClassicMovieAsync(page) }
    }
).flow
```

- [ ] **Step 3: Compile to verify syntax and types**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat :app:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit changes**

```bash
git add app/src/main/java/com/example/movie/repository/MovieRepository.kt app/src/main/java/com/example/movie/repository/MovieRepositoryImpl.kt
git commit -m "feat: add getClassicMoviesPaging to MovieRepository"
```

---

### Task 4: Refactor `ClassicMoviesViewModel` to Use Paging 3

**Files:**
- Modify: `app/src/main/java/com/example/movie/viewmodel/ClassicMoviesViewModel.kt`
- Create: `app/src/test/java/com/example/movie/viewmodel/ClassicMoviesViewModelTest.kt`

**Interfaces:**
- Consumes: `MovieRepository.getClassicMoviesPaging()`
- Produces: `ClassicMoviesViewModel.movies: Flow<PagingData<Movie>>`

- [ ] **Step 1: Write unit test in `ClassicMoviesViewModelTest.kt`**

Test verifying `viewModel.movies` is exposed from repository and cached in `viewModelScope`.

- [ ] **Step 2: Refactor `ClassicMoviesViewModel.kt`**

Streamline the class:
```kotlin
package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie.model.Movie
import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ClassicMoviesViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = movieRepository
        .getClassicMoviesPaging()
        .cachedIn(viewModelScope)
}
```

- [ ] **Step 3: Run unit tests**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat :app:testDebugUnitTest --tests "com.example.movie.viewmodel.ClassicMoviesViewModelTest"
```
Expected: PASS.

- [ ] **Step 4: Commit changes**

```bash
git add app/src/main/java/com/example/movie/viewmodel/ClassicMoviesViewModel.kt app/src/test/java/com/example/movie/viewmodel/ClassicMoviesViewModelTest.kt
git commit -m "refactor: simplify ClassicMoviesViewModel with Paging 3"
```

---

### Task 5: Implement Shimmer Loading and Refactor `ClassicMovieListRow.kt` to Consume Paging 3

**Files:**
- Create: `app/src/main/java/com/example/movie/ui/components/Shimmer.kt`
- Modify: `app/src/main/java/com/example/movie/ui/components/ClassicMoviesListRow.kt`

**Interfaces:**
- Consumes: `ClassicMoviesViewModel.movies: Flow<PagingData<Movie>>`
- Produces: `Modifier.shimmerEffect()`, `MovieRowShimmer()`, `ClassicMovieListRow`, `ClassicMovieList`

- [ ] **Step 1: Create `app/src/main/java/com/example/movie/ui/components/Shimmer.kt`**

Implement reusable animated gradient shimmer modifier and `MovieRowShimmer`:
```kotlin
package com.example.movie.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )
    val shimmerColors = listOf(
        Color.DarkGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.DarkGray.copy(alpha = 0.5f),
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
    background(brush)
}

@Composable
fun MovieRowShimmer(
    modifier: Modifier = Modifier,
    cardCount: Int = 5
) {
    LazyRow(
        modifier = modifier.height(185.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
        userScrollEnabled = false
    ) {
        items(cardCount) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(185.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}
```

- [ ] **Step 2: Update `ClassicMoviesListRow.kt` to use Shimmer on initial loading and consume `LazyPagingItems`**

- Collect `val movies = viewModel.movies.collectAsLazyPagingItems()`
- Set up pull-to-refresh:
  ```kotlin
  val isRefreshing = movies.loadState.refresh is LoadState.Loading && movies.itemCount > 0
  val pullRefreshState = rememberPullRefreshState(
      refreshing = isRefreshing,
      onRefresh = { movies.refresh() }
  )
  ```
- Render `MovieRowShimmer` when `movies.loadState.refresh is LoadState.Loading && movies.itemCount == 0`:
  ```kotlin
  if (movies.loadState.refresh is LoadState.Loading && movies.itemCount == 0) {
      MovieRowShimmer(modifier = modifier)
  } else {
      LazyRow(
          modifier = modifier.height(185.dp),
          state = lazyListState,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
      ) {
          items(
              count = movies.itemCount,
              key = movies.itemKey { it.id }
          ) { index ->
              val movie = movies[index]
              if (movie?.posterImage != null) {
                  MovieCard(
                      modifier = modifier.width(150.dp),
                      movie = movie,
                      onMovieClick = { actions.goToMovieDetail(it) }
                  )
              }
          }

          if (movies.loadState.append is LoadState.Loading) {
              item {
                  Row(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(16.dp),
                      horizontalArrangement = Arrangement.Center
                  ) {
                      CircularProgressIndicator(color = White, modifier = Modifier.size(48.dp))
                  }
              }
          }
      }
  }
  ```
- Remove `InfiniteListHandler`.

- [ ] **Step 3: Run full build and unit tests**

Run:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat testDebugUnitTest assembleDebug
```
Expected: BUILD SUCCESSFUL with 0 compilation errors and all tests passing.

- [ ] **Step 4: Commit changes**

```bash
git add app/src/main/java/com/example/movie/ui/components/Shimmer.kt app/src/main/java/com/example/movie/ui/components/ClassicMoviesListRow.kt
git commit -m "feat: add Shimmer loading and migrate ClassicMovieListRow to LazyPagingItems"
```

