package com.example.movie.ui.components

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movie.R

@Composable
fun dimensionResource(@DimenRes id: Int): Dp {
    val isInEditMode = LocalInspectionMode.current
    return if (isInEditMode) {
        when (id) {
            R.dimen.dimen_1 -> 1.dp
            R.dimen.dimen_2 -> 2.dp
            R.dimen.dimen_4 -> 4.dp
            R.dimen.dimen_6 -> 6.dp
            R.dimen.dimen_10 -> 10.dp
            R.dimen.dimen_12 -> 12.dp
            R.dimen.dimen_14 -> 14.dp
            R.dimen.dimen_30 -> 30.dp
            R.dimen.dimen_48 -> 48.dp
            R.dimen.dimen_50 -> 50.dp
            R.dimen.dimen_70 -> 70.dp
            R.dimen.dimen_75 -> 75.dp
            R.dimen.dimen_80 -> 80.dp
            R.dimen.dimen_100 -> 100.dp
            R.dimen.dimen_130 -> 130.dp
            R.dimen.dimen_150 -> 150.dp
            R.dimen.dimen_200 -> 200.dp
            else -> 0.dp
        }
    } else {
        androidx.compose.ui.res.dimensionResource(id)
    }
}

@Composable
fun textSizeResource(@DimenRes id: Int): TextUnit {
    val isInEditMode = LocalInspectionMode.current
    return if (isInEditMode) {
        when (id) {
            R.dimen.text_size_10 -> 10.sp
            R.dimen.text_size_12 -> 12.sp
            R.dimen.text_size_13 -> 13.sp
            R.dimen.text_size_14 -> 14.sp
            R.dimen.text_size_15 -> 15.sp
            R.dimen.text_size_16 -> 16.sp
            R.dimen.text_size_18 -> 18.sp
            R.dimen.text_size_25 -> 25.sp
            else -> 16.sp
        }
    } else {
        with(LocalDensity.current) { dimensionResource(id).toSp() }
    }
}

