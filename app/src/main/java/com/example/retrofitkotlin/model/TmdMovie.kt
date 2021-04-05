package com.example.retrofitkotlin.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.retrofitkotlin.util.CategoryEnum
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class TmdMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String?
) : Parcelable

data class TmdbMovieResponse(
    val results: List<TmdMovie>
)
