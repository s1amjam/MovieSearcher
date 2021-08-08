package com.moviesearcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieSearcherActivityTest : BaseTest() {
    @Test
    fun fromMainFragmentToMovieInfoTest() {
        Thread.sleep(5000)
        val recyclerView = onView(
            Matchers.allOf(
                withId(R.id.movie_recycler_view),
                childAtPosition(
                    withId(R.id.movie_constraint_layout),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(5000)
        val viewGroup = onView(
            Matchers.allOf(
                withId(R.id.movie_info_constraint_layout),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        withId(R.id.nav_host_container),
                        ViewMatchers.withParent(withId(R.id.nav_host_container))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        viewGroup.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

