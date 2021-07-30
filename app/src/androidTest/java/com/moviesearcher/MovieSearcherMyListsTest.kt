package com.moviesearcher


import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieSearcherMyListsTest : BaseTest() {
    @Test
    fun openMyListsTest() {
        val appCompatImageButton3 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val navigationMenuItemView3 = onView(
            allOf(
                withId(R.id.menu_item_my_lists),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())
        Thread.sleep(10000)
        val imageView = onView(
            allOf(
                withId(R.id.image_view_my_lists_item),
                ViewMatchers.withParent(
                    allOf(
                        withId(R.id.material_card_view_constraint_layout_my_lists_item),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(CardView::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageView.check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun openMovieFromMyListTest() {
        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
            allOf(
                withId(R.id.menu_item_my_lists),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())

        val materialCardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.my_lists_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_my_lists_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView.perform(click())

        val materialCardView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.my_list_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_my_list_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView2.perform(click())

        val viewGroup = onView(
            allOf(
                withId(R.id.movie_info_constraint_layout),
                ViewMatchers.withParent(
                    allOf(
                        withId(R.id.nav_host_container),
                        ViewMatchers.withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(ViewAssertions.matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.menu_button_add_movie_to_list), ViewMatchers.withText("ADD TO LIST"),
                ViewMatchers.withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        ViewMatchers.withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.button_mark_movie_as_favorite),
                ViewMatchers.withText("MARK AS FAVORITE"),
                ViewMatchers.withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        ViewMatchers.withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(ViewAssertions.matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.button_watchlist), ViewMatchers.withText("ADD TO WATCHLIST"),
                ViewMatchers.withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        ViewMatchers.withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button3.check(ViewAssertions.matches(isDisplayed()))
    }
}
