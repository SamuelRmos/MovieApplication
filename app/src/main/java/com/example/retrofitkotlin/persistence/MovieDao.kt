package com.example.retrofitkotlin.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofitkotlin.model.TmdMovie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movies: MutableList<TmdMovie>?)

    @Query("SELECT * FROM TmdMovie WHERE id = :id_")
    fun getMovie(id_: Int): TmdMovie

    @Query("SELECT * FROM TmdMovie")
    fun getMovieList(): MutableList<TmdMovie>
}