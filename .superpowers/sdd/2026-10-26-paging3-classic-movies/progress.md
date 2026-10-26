# SDD ledger — plan: docs/superpowers/plans/2026-10-26-paging3-classic-movies.md

Pre-flight: interfaces checked. Tasks:
- Task 1: Paging 3 dependencies.
- Task 2: MoviePagingSource (consumes MovieApi, produces MoviePagingSource).
- Task 3: MovieRepository (consumes MoviePagingSource, produces getClassicMoviesPaging).
- Task 4: ClassicMoviesViewModel (consumes getClassicMoviesPaging, produces movies: Flow<PagingData<Movie>>).
- Task 5: Shimmer + ClassicMovieListRow (consumes movies: Flow<PagingData<Movie>>, produces UI with shimmer on initial load).

Task 1: complete (commit c15dfbb, dependencies resolved)
