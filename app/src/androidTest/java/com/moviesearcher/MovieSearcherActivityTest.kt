package com.moviesearcher

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//TODO: rework all Thread.sleep's
@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieSearcherActivityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MovieSearcherActivity::class.java)

    @Test
    fun movieSearcherActivityTest() {
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

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

