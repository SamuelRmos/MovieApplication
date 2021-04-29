package com.example.movieapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.converters.Converters
import com.example.movieapp.model.Movie

@Database(entities = [Movie::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}