package com.example.commons.converters

import androidx.room.TypeConverter
import com.example.commons.util.CategoryEnum

class Converters {
    @TypeConverter
    fun fromCategory(value: CategoryEnum): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toCategory(value: Int) = when (value) {
        1 -> CategoryEnum.POPULAR
        2 -> CategoryEnum.RATED
        3 -> CategoryEnum.TODAY
        else -> CategoryEnum.CLASSIC
    }

}