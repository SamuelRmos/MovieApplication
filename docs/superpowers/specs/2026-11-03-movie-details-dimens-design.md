# Design Document: Extract Dimensions in MovieDetailsScreen

**Date:** 2026-11-03  
**Status:** In Review  
**Topic:** Extracting `dp` and `sp` dimensions in `MovieDetailsScreen.kt` to `dimens.xml` using generic resource names

---

## 1. Intent and Why
`MovieDetailsScreen.kt` currently contains hardcoded `.dp` and `.sp` dimension literals scattered across its layout, spacing, padding, image sizes, and text components. Extracting these values into `app/src/main/res/values/dimens.xml` with generic names centralizes dimension definitions and enables reuse across `MovieDetailsScreen` (and other screens in the app).

## 2. Goals, Non-Goals, and Anti-Goals
- **Goals:**
  - Define generic `dp` dimension resources (`dimen_4`, `dimen_10`, `dimen_12`, `dimen_30`, `dimen_48`, `dimen_70`, `dimen_80`, `dimen_100`, `dimen_130`, `dimen_150`, `dimen_200`) in `dimens.xml`.
  - Define generic `sp` text size resources (`text_size_10`, `text_size_12`, `text_size_13`, `text_size_15`, `text_size_16`, `text_size_25`) in `dimens.xml`.
  - Provide a clean `@Composable` helper (e.g., `textSizeResource(@DimenRes id: Int): TextUnit`) using `LocalDensity.current` to read `sp` dimensions from `dimens.xml` in Compose.
  - Replace all hardcoded `.dp` and `.sp` values in `MovieDetailsScreen.kt` with `dimensionResource(...)` and `textSizeResource(...)`.
- **Non-Goals:**
  - Refactoring dimensions in other screens outside `MovieDetailsScreen.kt`.
- **Anti-Goals:**
  - Changing the visual appearance, layout proportions, or text sizes of `MovieDetailsScreen`.

## 3. Constraints
- Jetpack Compose's standard `dimensionResource(@DimenRes id: Int)` returns `Dp`, so `TextUnit` (`sp`) conversion must use `with(LocalDensity.current) { dimensionResource(id).toSp() }`.
- Existing entries in `dimens.xml` (`margin_normal`, `anim_duration_long`) must be preserved.

## 4. Approaches Considered
1. **Approach 1: Extract `dp` dimensions only using `dimensionResource()`**
   - *Trade-offs:* Uses built-in Compose `dimensionResource()` without a density helper, but leaves `.sp` literals hardcoded in `MovieDetailsScreen.kt`.
2. **Approach 2: Extract both `dp` and `sp` dimensions into `dimens.xml` with a Compose `sp` helper (Selected)**
   - *Trade-offs:* Requires a small `@Composable` helper to convert `dimensionResource(id)` to `TextUnit` via `LocalDensity.current`, but completely eliminates all hardcoded `.dp` and `.sp` values in `MovieDetailsScreen.kt` as requested.

## 5. Implementation Details
- **`app/src/main/res/values/dimens.xml`**:
  - `dimen_4` (`4dp`), `dimen_10` (`10dp`), `dimen_12` (`12dp`), `dimen_30` (`30dp`), `dimen_48` (`48dp`), `dimen_70` (`70dp`), `dimen_80` (`80dp`), `dimen_100` (`100dp`), `dimen_130` (`130dp`), `dimen_150` (`150dp`), `dimen_200` (`200dp`).
  - `text_size_10` (`10sp`), `text_size_12` (`12sp`), `text_size_13` (`13sp`), `text_size_15` (`15sp`), `text_size_16` (`16sp`), `text_size_25` (`25sp`).
- **`MovieDetailsScreen.kt`**:
  - Add `@Composable private fun textSizeResource(@DimenRes id: Int): TextUnit = with(LocalDensity.current) { dimensionResource(id).toSp() }`.
  - Replace every `.dp` call with `dimensionResource(R.dimen.dimen_*)` and every `.sp` call with `textSizeResource(R.dimen.text_size_*)`.

## 6. What's Left to the Builder
- Clean up unused `.dp` and `.sp` imports in `MovieDetailsScreen.kt` after replacement.

