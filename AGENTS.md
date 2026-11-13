# AGENTS.md — CineBook (`Movies`) Project Guide

This document provides baseline context, architectural guidelines, and engineering standards for all AI agents and contributors working in the **Movies (`com.samuelrmos.cinebook`)** repository.

---

## 1. General Rules

- **Preserve Existing Behavior & Contracts:** Do not alter Retrofit API contracts (`MovieApi`), navigation routes (`Screens`), or UI state data classes unless explicitly requested.
- **Structured Concurrency & Thread Safety:**
  - Perform network, disk, and heavy CPU work off the main thread (`Dispatchers.IO` for network/repository flows via `.flowOn(Dispatchers.IO)`, `Dispatchers.Default` for bitmap/palette processing).
  - Always rethrow `CancellationException` before catching generic `Exception` inside coroutines and cold `Flow` builders so cancelled jobs terminate immediately.
- **No Magic Dimensions or Strings in New/Refactored UI:**
  - Extract dimension values (`dp` and `sp`) to `app/src/main/res/values/dimens.xml` using generic resource names (`dimen_*`, `text_size_*`) and reference them via `dimensionResource(R.dimen.*)` and `textSizeResource(R.dimen.*)`.
  - Extract user-facing strings to `app/src/main/res/values/strings.xml` and reference them via `stringResource(R.string.*)`.
- **Hardware Bitmap Safety:** When inspecting pixels or generating a `Palette` from a Coil-loaded `Bitmap` on API 28+, always convert `Bitmap.Config.HARDWARE` bitmaps to `Bitmap.Config.ARGB_8888` (`bitmap.copy(Bitmap.Config.ARGB_8888, false)`) first.
- **Recomposition Safety:** Never trigger ViewModel side effects (network fetches, analytics, or callbacks) directly in a `@Composable` function body; wrap them in `LaunchedEffect(key)`.

---

## 2. Build Commands

Run all Gradle commands from the repository root using the Gradle wrapper (`./gradlew` on macOS/Linux/Git Bash, `.\gradlew.bat` on Windows PowerShell):

```powershell
# Compile debug Kotlin sources
.\gradlew.bat compileDebugKotlin

# Assemble debug APK
.\gradlew.bat assembleDebug

# Run all JVM unit tests across modules
.\gradlew.bat testDebugUnitTest

# Run a single unit test class
.\gradlew.bat :app:testDebugUnitTest --tests "com.example.movie.viewmodel.SearchViewModelTest"

# Run Ktlint code style checks
.\gradlew.bat ktlintCheck

# Auto-format code with Ktlint
.\gradlew.bat ktlintFormat

# Run Android Lint
.\gradlew.bat lintDebug
```

> **Note:** Build types read `System.getenv("API_KEY")` to populate `BuildConfig.TMDB_API_KEY`. Ensure `JAVA_HOME` points to a valid JDK / Android Studio JBR installation.

---

## 3. Module Architecture

The project is a multi-module Android application (`compileSdk = 34`, `minSdk = 28`, `targetSdk = 34`) configured in `settings.gradle.kts`:

### `:app` (`com.example.movie`)
The primary application module containing presentation, domain models, data repositories, network services, and dependency injection:
- `di/` — Hilt modules (`ApiModule`, `RepositoryModule`, `PersistenceModule`) installed in `SingletonComponent`.
- `network/` — Retrofit `MovieApi` interface communicating with TMDB v3 (`https://api.themoviedb.org/3/`).
- `data/paging/` — Generic Paging 3 `MoviePagingSource` enforcing a 300-movie pagination cap (`MOVIES_LIMIT = 300`).
- `repository/` — Repository interfaces and implementations (`MovieRepository` / `MovieRepositoryImpl`, `DetailRepository` / `DetailRepositoryImpl`, `SearchRepository` / `SearchRepositoryImpl`).
- `model/` — Moshi-annotated (`@JsonClass(generateAdapter = true)`) models (`Movie`, `MovieCredits`, `Cast`, `Crew`, `Video`, `SearchMovieItem`, `Constants`, `MockedMovieData`).
- `persistence/` — Room database (`AppDataBase`, `MovieDao`).
- `viewmodel/` — `@HiltViewModel` classes managing screen state (`PopularMoviesViewModel`, `RatedMovieViewModel`, `TodayMoviesViewModel`, `ClassicMoviesViewModel`, `MovieDetailViewModel`, `SearchViewModel`, `RootViewModel`).
- `navigation/` — Compose Navigation setup (`Screens`, `Actions`, `NavGraph`).
- `ui/` — Jetpack Compose screens and reusable UI components (`MoviesScreen`, `MovieCard`, `ui/components/`, `ui/details/`, `ui/search/`, `ui/state/`).
- `theme/` — Material 3 theme definition (`MovieTheme`, `Color.kt`, `Type.kt`, `Shape.kt`).

### `:commons` (`com.example.commons`)
Shared library module providing cross-cutting utilities:
- `converters/` — Room `@TypeConverter` classes (`Converters`, `ListConverters`).
- `functional/` — Functional types (`Either`).
- `log/` — Logging utilities (`MovieLog`).
- `service/` — Connectivity helpers (`ConnectionService`).
- `util/` — Shared enums and wrappers (`CategoryEnum`, `Result`).

---

## 4. Screen Architecture

The UI follows a single-activity (`MovieActivity` -> `MovieApp` -> `NavGraph`) **MVVM + Unidirectional Data Flow (UDF)** architecture built with Jetpack Compose and Hilt:

1. **Navigation (`navigation/`):**
   - Routes are defined in sealed class `Screens`:
     - `Screens.MoviesScreen` (`"movie_screen"`) — Home screen with horizontal movie carousels.
     - `Screens.MovieDetails` (`"detail_screen"`) — Movie detail screen receiving a `Parcelable` `Movie` argument (`"movie_detail"`).
     - `Screens.SearchScreen` (`"search_screen"`) — Debounced search screen.
   - Navigation actions are encapsulated in `Actions(navHostController)` (`goToMovieDetail`, `goToMovies`, `goToSearch`, `navigateUp`).
2. **Stateless vs. Stateful Composables:**
   - Top-level screen composables (e.g., `MovieDetailsScreen`, `SearchScreen`) obtain the `@HiltViewModel`, collect `StateFlow` via `collectAsState()`, handle `SideEffect` / `LaunchedEffect` lifecycle hooks, and delegate rendering to stateless content composables (`MovieDetailsContent`, `SearchScreenContent`).
   - Stateless `*Content` composables accept plain state objects (`DetailsState`, `SearchState`) and lambdas, enabling isolated `@Preview` functions without Hilt or network dependencies.

---

## 5. Use Cases & Data Flows

Currently, business logic and data orchestration live directly in the **Repository + ViewModel** layers (no separate UseCase classes unless complex multi-repository orchestration is introduced):

1. **Paginated Home Carousels (`MovieRepository` -> `*MoviesViewModel`):**
   - `MovieRepositoryImpl` wraps `MovieApi` endpoints (`getPopularMovieAsync`, `getRatedMovieAsync`, `getTodayMovieAsync`, `getClassicMovieAsync`) in a Paging 3 `Pager(PagingConfig(pageSize = 20))` using `MoviePagingSource`.
   - Each carousel ViewModel exposes `val movies: Flow<PagingData<Movie>> = movieRepository.get*Movies().cachedIn(viewModelScope)`.
   - Carousel UI rows (`PopularMoviesListRow`, `RatedMovieListRow`, `TodayMoviesListRow`, `ClassicMoviesListRow`) collect `LazyPagingItems`, rendering `MovieRowShimmer` during initial load, `ErrorRow` on failure, and `LazyRow` of `MovieCard` items with pull-to-refresh.
2. **Movie Details & Concurrent Credits/Trailer Fetch (`DetailRepository` -> `MovieDetailViewModel`):**
   - `DetailRepositoryImpl` wraps `getCreditsMovie(id)` and `getVideosMovie(id)` in a generic `safeApiCall<T>` emitting `DetailRequestState<T>` (`Loading`, `Success<T>`, `Error`).
   - `MovieDetailViewModel` combines both flows concurrently via `combine(creditsFlow, videoFlow, ::mergeStates)`, deduplicates requests by `movieId`, and updates `StateFlow<DetailsState>`.
3. **Movie Search & Parallel Director Enrichment (`SearchRepository` -> `SearchViewModel`):**
   - `SearchViewModel` debounces user input (`350ms`), cancels stale jobs on new keystrokes, and clears state immediately on blank queries.
   - `SearchRepositoryImpl` calls `movieApi.searchMovies(query)` and concurrently resolves each movie's director (`job == "Director"`) via `async` + `getCreditsMovie(movie.id)` backed by an in-memory `ConcurrentHashMap<Int, String>` cache.

---

## 6. UI & Design System

- **Theme (`theme/`):**
  - Dark-first cinema palette defined in `Color.kt`:
    - `colorPrimary = Color(0xff000000)`
    - `colorAccent = Color(0xffff0b04)`
    - `colorText = Color(0xffffffff)`
    - `colorBackground = Color(0xff424242)`
  - Typography in `Type.kt` uses the custom `Roboto` font family (`R.font.roboto`).
- **Dynamic Palette Theming (`MovieDetailsScreen.kt`):**
  - Uses `androidx.palette:palette-ktx` (`Palette.from(softwareBitmap).generate().dominantSwatch`) on `Dispatchers.Default` to adapt the detail screen's background color to the movie backdrop's predominant color.
  - Text color automatically switches between `Color.White` (when `backgroundColor.luminance() < 0.5f`) and `Color.Black` (when `>= 0.5f`) for high contrast.
  - The full-screen `Loading()` indicator remains visible until both network state (`!requestState.isLoading`) and palette extraction (`isPaletteReady`) have completed.
- **Image Loading:**
  - Uses Landscapist Coil (`com.skydoves.landscapist.coil.CoilImage`) with TMDB image URL builders in `Constants.kt`:
    - `Constants.artworkUrl(path)` (`w1280` for backdrops)
    - `Constants.artworkDetailImagePoster(path)` (`w300` for detail/cast/search posters)
    - `Constants.artworkImagePoster(path)` (`w500` for carousel cards)
  - Always provide `previewPlaceholder = placeholderImage(id)` for Compose `@Preview` support.

---

## 7. Testing

- **Location:** `app/src/test/java/com/example/movie/`
- **Stack:** JUnit 4 (`org.junit.Test`, `org.junit.Assert.*`), MockK (`io.mockk.mockk`, `every`, `coEvery`, `verify`, `coVerify`), OkHttp `MockWebServer`, and AndroidX Paging Testing.
- **ViewModel Dispatcher Injection Pattern:**
  - Because `kotlinx-coroutines-test` is not in the dependency graph, ViewModels that launch coroutines in `viewModelScope` should provide a primary constructor accepting a `CoroutineDispatcher` (and optional timing parameters like `debounceMs`) plus a secondary `@Inject constructor(...)` defaulting to `Dispatchers.IO`.
  - In unit tests, instantiate the ViewModel with `Dispatchers.Unconfined` (and `debounceMs = 0L` where applicable) for fast, deterministic synchronous assertions.
- **Test Naming Convention:** `methodUnderTest_condition_expectedResult` (e.g., `getMovieCredits_whenPartialError_preservesSuccessDataAndSetsError`).

---

## 8. Conventions

- **Dependency Management:** Declare all library versions, artifacts, and plugins in `gradle/libs.versions.toml` and reference them via `libs.*` in `build.gradle.kts`.
- **Dimensions & Text Sizes:**
  - Add reusable `dp` dimensions as `<dimen name="dimen_<value>"><value>dp</dimen>` and `sp` font sizes as `<dimen name="text_size_<value>"><value>sp</dimen>` in `app/src/main/res/values/dimens.xml`.
  - Convert `sp` dimension resources in Compose using:
    ```kotlin
    @Composable
    private fun textSizeResource(@DimenRes id: Int): TextUnit =
        with(LocalDensity.current) { dimensionResource(id).toSp() }
    ```
- **Null Safety:** Avoid non-null assertions (`!!`). Use `.orEmpty()`, safe calls (`?.`), `let`, or sealed generic states (`DetailRequestState<out T>`).
- **Logging:** Use `Timber.e(exception)` rather than `printStackTrace()` or `Log.e`.

