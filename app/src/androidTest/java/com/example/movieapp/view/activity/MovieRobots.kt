package com.example.movieapp.view.activity

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movieapp.R
import com.example.movieapp.view.adapter.MovieAdapter
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieRobots {

    val intent = Intent(ApplicationProvider.getApplicationContext(), MovieActivity::class.java)

    fun mostPopularRecyclerView(): ViewInteraction {
        return onView(withId(R.id.rv_mostPopular))
    }

    fun movieDetailsFragment(): ViewInteraction {
        return onView(withId(R.id.movieDetailsFragment))
    }

    fun scrollMostPopular(): MovieRobots {
        mostPopularRecyclerView()
            .perform(scrollToPosition<MovieAdapter.ViewHolder>(19))
        return this
    }

    fun clickItemMostPopularRecyclerView(): MovieRobots {
        mostPopularRecyclerView()
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                    5,
                    click()
                )
            )
        return this
    }

    fun checkShowMovieDetailsFragment(): MovieRobots {
        movieDetailsFragment().check(matches(isDisplayed()))
        return this
    }
}