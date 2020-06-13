package com.example.retrofitkotlin.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class AppDataBaseTest {
    //region constants

    //end region constants

    //region helper fields

    lateinit var appDataBase: AppDataBase

    // end region helper fields

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDataBase = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
    }

    @After
    fun closeDb() {
        appDataBase.close()
    }

    // region helper methods

    // end region helper methods

    // region helper class

    // end region helper class
}