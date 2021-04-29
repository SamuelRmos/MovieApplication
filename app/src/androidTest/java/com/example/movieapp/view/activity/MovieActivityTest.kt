package com.example.movieapp.view.activity

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieActivityTest {

    lateinit var scenario: ActivityScenario<MovieActivity>
    lateinit var robots: MovieRobots

    @Before
    fun setup() {
        robots = MovieRobots()
        scenario = launch(robots.intent)
    }

    @Test
    fun check_MostPopular_RecyclerView() {
        SystemClock.sleep(10000)
        robots.scrollMostPopular()
        SystemClock.sleep(1000)
        robots.clickItemMostPopularRecyclerView()
        robots.checkShowMovieDetailsFragment()
        SystemClock.sleep(1500)
    }

    @After
    fun tearDown() {
        scenario.close()
    }
}