# MovieDetailsScreen Dimension Extraction Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extract all hardcoded `dp` and `sp` dimension values in `MovieDetailsScreen.kt` into `app/src/main/res/values/dimens.xml` using generic resource names and reference them via `dimensionResource` and a `textSizeResource` composable helper.

**Architecture:** Add generic `<dimen>` entries (`dimen_*` for `dp` and `text_size_*` for `sp`) to `dimens.xml`. In `MovieDetailsScreen.kt`, define a private `@Composable fun textSizeResource(@DimenRes id: Int): TextUnit` converting `dimensionResource(id)` to `TextUnit` via `LocalDensity.current`, and replace every `.dp` and `.sp` literal with resource lookups.

**Tech Stack:** Kotlin, Jetpack Compose, Android XML Resources

**Spec:** `docs/superpowers/specs/2026-11-03-movie-details-dimens-design.md`

## Global Constraints

- Jetpack Compose's standard `dimensionResource(@DimenRes id: Int)` returns `Dp`, so `TextUnit` (`sp`) conversion must use `with(LocalDensity.current) { dimensionResource(id).toSp() }`.
- Existing entries in `dimens.xml` (`margin_normal`, `anim_duration_long`) must be preserved.
- Do not change visual appearance, layout proportions, or text sizes of `MovieDetailsScreen`.

## Review Focus

- Missing or mismatched `<dimen>` resource names between `dimens.xml` and `MovieDetailsScreen.kt` R.dimen references.
- Remaining hardcoded `.dp` or `.sp` literals anywhere in `MovieDetailsScreen.kt`.
- Unused `androidx.compose.ui.unit.dp` or `androidx.compose.ui.unit.sp` imports left behind in `MovieDetailsScreen.kt`.

---

### Task 1: Define Generic `dp` and `sp` Resources in `dimens.xml` and Refactor `MovieDetailsScreen.kt`

**Files:**
- Modify: `app/src/main/res/values/dimens.xml`
- Modify: `app/src/main/java/com/example/movie/ui/details/MovieDetailsScreen.kt`

**Interfaces:**
- Consumes: `androidx.compose.ui.res.dimensionResource`, `androidx.compose.ui.platform.LocalDensity`, `com.example.movie.R`
- Produces: `@Composable private fun textSizeResource(@DimenRes id: Int): TextUnit` in `MovieDetailsScreen.kt`, `<dimen>` entries `dimen_4`, `dimen_10`, `dimen_12`, `dimen_14`, `dimen_30`, `dimen_48`, `dimen_70`, `dimen_80`, `dimen_100`, `dimen_130`, `dimen_150`, `dimen_200`, `text_size_10`, `text_size_12`, `text_size_13`, `text_size_15`, `text_size_16`, `text_size_25` in `dimens.xml`

- [x] **Step 1: Add generic `dp` and `sp` `<dimen>` entries to `app/src/main/res/values/dimens.xml`**

- [x] **Step 2: Add `textSizeResource` helper and replace all `.dp` and `.sp` literals in `app/src/main/java/com/example/movie/ui/details/MovieDetailsScreen.kt`**

- [x] **Step 3: Verify no hardcoded `.dp` or `.sp` remain in `MovieDetailsScreen.kt` and all `R.dimen` references match `dimens.xml`**
