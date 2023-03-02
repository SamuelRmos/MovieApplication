//package com.example.retrofitkotlin.view.activity
//
//import android.os.SystemClock
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.rule.ActivityTestRule
//import com.example.retrofitkotlin.R
//import com.example.retrofitkotlin.base.BaseUITest
//import com.example.retrofitkotlin.view.adapter.MovieAdapter
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class MovieActivityTest : BaseUITest() {
//
//    //region constants
//
//    //end region constants
//
//    //region helper fields
//
//    @Rule
//    @JvmField
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Rule
//    @JvmField
//    var mActivityTestRule = ActivityTestRule(
//        MovieActivity::class.java, true, false
//    )
//
//    private val mTitle = "Ad Astra"
//    private val mOverview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown."
//
//    // end region helper fields
//
//    @Before
//    fun setup() {
//        super.setUp()
//        createApi()
//    }
//
//    @Test
//    fun movieActivityTest_recyclerView_expectedResponse() {
//
//        mActivityTestRule.launchActivity(null)
//
//        SystemClock.sleep(1000)
//
//        onView(withId(R.id.recyclerView))
//            .perform(actionOnItemAtPosition<MovieAdapter.ViewHolder>(0, click()))
//
//        onView(withId(R.id.tvtitle))
//            .check(matches(withText(mTitle)))
//
//        onView(withId(R.id.tvdetail2))
//            .check(matches(withText(mOverview)))
//
//        SystemClock.sleep(1000)
//
//    }
//
//    // region helper methods
//
//    // end region helper methods
//
//    // region helper class
//
//    // end region helper class
//}