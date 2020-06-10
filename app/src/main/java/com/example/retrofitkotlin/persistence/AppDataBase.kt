package com.example.retrofitkotlin.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.retrofitkotlin.model.TmdMovie

@Database(entities = [TmdMovie::class], version = 3, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){

    abstract fun movieDao(): MovieDao
}