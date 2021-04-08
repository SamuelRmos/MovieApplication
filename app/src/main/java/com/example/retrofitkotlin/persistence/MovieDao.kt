package com.example.retrofitkotlin.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofitkotlin.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movies: MutableList<Movie>?)

    @Query("SELECT * FROM Movie WHERE id = :id_")
    fun getMovie(id_: Int): Movie

    @Query("SELECT * FROM Movie")
    fun getMovieList(): MutableList<Movie>
}