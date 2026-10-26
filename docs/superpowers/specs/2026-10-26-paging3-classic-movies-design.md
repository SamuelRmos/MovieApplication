# Design Document: Paging 3 Migration for ClassicMoviesViewModel

**Date:** 2026-10-26  
**Status:** In Review  
**Topic:** Migrating `ClassicMoviesViewModel` and its UI carousel to AndroidX Paging 3

---

## 1. Intent and Why

The current implementation of `ClassicMoviesViewModel` handles pagination and UI state using three disjoint `StateFlow` streams (`stateClassicMovie`, `paginationClassicState`, and `isRefresh`). This setup suffers from several critical defects:
- **Race conditions & concurrency leaks:** `getMoviesPaginated()` does not guard against duplicate concurrent network requests during fast fling/scroll operations. Rapid triggering from `InfiniteListHandler` spawns overlapping coroutines without job tracking or cancellation.
- **Flawed refresh lifecycle:** `refresh()` sets `isRefresh` back to `false` synchronously before the coroutine finishes fetching, resets the page pointer `skip` to 0 instead of 1, and appends to the old list instead of replacing it.
- **State consistency & threading:** ViewModels launch coroutines explicitly on `Dispatchers.IO` and perform non-atomic state reads before `.update`.

Migrating to **AndroidX Paging 3** resolves these issues natively. Paging 3 provides built-in deduplication, memory caching within `viewModelScope`, automatic cancellation of stale pagination jobs, out-of-the-box loading/error states for both initial load and append operations, and first-class Compose integration (`collectAsLazyPagingItems`).

---

## 2. Goals, Non-Goals, and Anti-Goals

### Goals
- Add AndroidX Paging 3 (`paging-runtime` and `paging-compose`) to version catalogs and Gradle build scripts.
- Implement a reusable, generic `MoviePagingSource` capable of wrapping any paginated endpoint in `MovieApi`.
- Enforce the 300-movie maximum cap (`MOVIES_LIMIT = 300`) inside the pagination boundary.
- Refactor `ClassicMoviesViewModel` to expose a single cached stream: `Flow<PagingData<Movie>>`.
- Refactor `ClassicMovieListRow.kt` to consume `LazyPagingItems<Movie>`, handling initial loading, pagination spinner, error retries, and pull-to-refresh without `InfiniteListHandler`.
- Provide unit tests verifying the `MoviePagingSource` logic (success page transition, end-of-pagination at 300 items / empty response, and error handling).

### Non-Goals
- Refactoring `PopularMoviesViewModel`, `RatedMovieViewModel`, or `TodayMoviesViewModel` in this initial migration step. (The `MoviePagingSource` will be reusable, but the other ViewModels will remain untouched for now).
- Introducing a Room offline database cache / `RemoteMediator` (the app currently operates purely via direct network requests).
- Modifying backend API query signatures or response data classes.

### Anti-Goals
- Altering the visual presentation, card layouts, animations, or styling in the UI.
- Retaining obsolete "zombie" state flows (`stateClassicMovie`, `paginationClassicState`, `isRefresh`) in `ClassicMoviesViewModel`.

---

## 3. Constraints

- **Platform:** Android (Min SDK 28, Target/Compile SDK 34).
- **Kotlin & Compose:** Kotlin 2.4.21, Jetpack Compose with Compose BOM `2024.10.00`.
- **Dependencies:** Managed centrally via `gradle/libs.versions.toml`. Compatible Paging 3 versions must be used (e.g., `3.3.0` or `3.2.1`).
- **Dependency Injection:** Hilt (`@HiltViewModel` and `@Inject`).

---

## 4. Approaches Considered

1. **Approach 1: Generic Paginator Component (Composition over Inheritance)**
   - *Description:* Build a custom `Paginator<Key, Item>` and unify state into a `MoviesUiState` data class.
   - *Trade-offs:* Lightweight with zero new library dependencies, but requires manual handling of caching, scroll listeners, and list diffing in Compose.
2. **Approach 2: Base Paginated ViewModel (Inheritance)**
   - *Description:* Create an abstract `BasePaginatedMovieViewModel : ViewModel()` implementing custom pagination logic.
   - *Trade-offs:* Quickly reduces boilerplate across carousels, but couples state to an inheritance hierarchy and retains custom pagination plumbing.
3. **Approach 3: AndroidX Paging 3 (Selected)**
   - *Description:* Use Google's official Paging 3 library with `PagingSource` and `Flow<PagingData<Movie>>`.
   - *Trade-offs:* Requires adding Paging dependencies and adapting the UI row to `LazyPagingItems`, but completely eliminates manual pagination boilerplate, guarantees thread safety and race condition immunity, and offers standard Android architecture support.

---

## 5. Architectural & Implementation Decisions

### 5.1 Dependencies (`gradle/libs.versions.toml` & `app/build.gradle.kts`)
- Add `pagingVersion = "3.3.0"` (or `3.2.1`).
- Define `paging-runtime = { group = "androidx.paging", name = "paging-runtime-ktx", version.ref = "pagingVersion" }`.
- Define `paging-compose = { group = "androidx.paging", name = "paging-compose", version.ref = "pagingVersion" }`.
- Define `paging-testing = { group = "androidx.paging", name = "paging-testing", version.ref = "pagingVersion" }` for unit tests.
- Add dependencies to `app/build.gradle.kts`.

### 5.2 Reusable PagingSource (`MoviePagingSource.kt`)
Create `com.example.movie.data.paging.MoviePagingSource`:
```kotlin
class MoviePagingSource(
    private val maxLimit: Int = MOVIES_LIMIT,
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

### 5.3 Repository Layer (`MovieRepository` & `MovieRepositoryImpl`)
Add method to `MovieRepository`:
```kotlin
fun getClassicMoviesPaging(): Flow<PagingData<Movie>>
```
Implemented in `MovieRepositoryImpl`:
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

### 5.4 ViewModel Layer (`ClassicMoviesViewModel.kt`)
Refactor `ClassicMoviesViewModel`:
```kotlin
@HiltViewModel
class ClassicMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = movieRepository
        .getClassicMoviesPaging()
        .cachedIn(viewModelScope)
}
```
All mutable flows, manual launches on `Dispatchers.IO`, race conditions, and synchronization bugs are eliminated.

### 5.5 UI Layer (`ClassicMoviesListRow.kt`)
Update `ClassicMovieListRow` and `ClassicMovieList`:
- Consume `val movies = viewModel.movies.collectAsLazyPagingItems()`.
- Initial loading: inspect `movies.loadState.refresh is LoadState.Loading`. When loading, display a shimmering placeholder row (`MovieCardShimmer`) with animated gradient brushes matching `MovieCard` dimensions (150.dp width, 185.dp height).
- Pagination loading: inspect `movies.loadState.append is LoadState.Loading`, displaying the `CircularProgressIndicator` item at the end of the row.
- Pull-to-refresh: wire `rememberPullRefreshState(refreshing = movies.loadState.refresh is LoadState.Loading, onRefresh = { movies.refresh() })`.
- Remove `InfiniteListHandler` as Paging 3 manages item prefetching automatically during scroll.

---

## 6. What's Left to the Builder

- Exact Paging 3 stable version resolution in Gradle to ensure compatibility with AGP and Kotlin versions.
- Unit test structure for `MoviePagingSource` and `ClassicMoviesViewModel` using MockK.
- Clean removal of unused imports and obsolete code from `ClassicMoviesViewModel.kt` and `ClassicMoviesListRow.kt`.
