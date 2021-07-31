package com.moviesearcher

import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {
    fun ts() = Thread.sleep(25000)

    @BeforeEach
    fun launchActivity() {
        ActivityScenario.launch(MovieSearcherActivity::class.java)
    }

    companion object Login : BaseTest() {
        @JvmStatic
        fun login() {
            launchActivity()
            openNavDrawer()

            if (withId(R.id.login_button) != null) {
                val navigationMenuItemView = onView(
                    allOf(
                        withId(R.id.login_button),
                        childAtPosition(
                            allOf(
                                withId(R.id.design_navigation_view),
                                childAtPosition(
                                    withId(R.id.nav_view),
                                    0
                                )
                            ),
                            1
                        ),
                        isDisplayed()
                    )
                )
                navigationMenuItemView.perform(click())
                ts()

                onWebView().withElement(
                    findElement(
                        Locator.ID,
                        "allow_authentication"
                    )
                ).perform(webClick())
                ts()

//                onWebView()
//                    .withElement(
//                        findElement(
//                            Locator.XPATH,
//                            "//*[@id=\"main\"]/section/div/div/div[1]/a/h2"
//                        )
//                    )
//                    .perform(webClick())
//                ts()
//                onWebView()
//                    .withElement(findElement(Locator.ID, "username"))
//                    .perform(webClick())
//                    .perform(DriverAtoms.webKeys("testmyapp"))
//                    .withElement(findElement(Locator.ID, "password"))
//                    .perform(webClick())
//                    .perform(DriverAtoms.webKeys("testmyapp123"))
//                    .withElement(findElement(Locator.ID, "login_button"))
//                    .perform(webClick())
//                    .withElement(
//                        findElement(
//                            Locator.ID,
//                            "allow_authentication"
//                        )
//                    )//deny_authentication
//                    .perform(webClick())
//                ts()


//                onWebView().withElement(
//                    findElement(
//                        Locator.ID,
//                        "allow_authentication"
//                    )
//                ).perform(webClick())
//                ts()
            }
        }


        @JvmStatic
        fun logout() {
            launchActivity()
            openNavDrawer()

            val logoutBtn = onView(
                allOf(
                    withId(R.id.logout_button),
                    childAtPosition(
                        allOf(
                            withId(R.id.design_navigation_view),
                            childAtPosition(
                                withId(R.id.nav_view),
                                0
                            )
                        ),
                        5
                    ),
                    isDisplayed()
                )
            )

            if (logoutBtn != null) {
                val navigationMenuItemView = onView(
                    allOf(
                        withId(R.id.logout_button),
                        childAtPosition(
                            allOf(
                                withId(R.id.design_navigation_view),
                                childAtPosition(
                                    withId(R.id.nav_view),
                                    0
                                )
                            ),
                            5
                        ),
                        isDisplayed()
                    )
                )
                navigationMenuItemView.perform(click())
                ts()
            }
        }
    }

    fun childAtPosition(
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

    open fun openNavDrawer() {
        val appCompatImageButton3 = onView(
            allOf(
                withContentDescription("Open navigation drawer"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(Matchers.`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())
    }

    fun openFavorites() {
        val navigationMenuItemView3 = onView(
            allOf(
                withId(R.id.menu_item_favorites),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())
        ts()
    }

    fun markMovieAsFavorite() {
        val materialButton4 = onView(
            allOf(
                withId(R.id.button_mark_movie_as_favorite),
                withText(R.string.mark_as_favorite),
                childAtPosition(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        ts()
        val button2 = onView(
            allOf(
                withId(R.id.button_mark_movie_as_favorite),
                withText(R.string.remove_from_favorite),
                withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
        ts()
    }

    fun markTvAsFavorite() {
        val materialButton4 = onView(
            allOf(
                withId(R.id.button_mark_tv_as_favorite),
                withText(R.string.mark_as_favorite),
                childAtPosition(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        ts()
        val button2 = onView(
            allOf(
                withId(R.id.button_mark_tv_as_favorite),
                withText(R.string.remove_from_favorite),
                withParent(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
        ts()
    }

    fun removeMovieFromFavorite() {
        val materialButton3 = onView(
            allOf(
                withId(R.id.button_mark_movie_as_favorite),
                withText(R.string.remove_from_favorite),
                childAtPosition(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        ts()
        val button = onView(
            allOf(
                withId(R.id.button_mark_movie_as_favorite),
                withText(R.string.mark_as_favorite),
                withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
        ts()
    }

    fun removeTvFromFavorite() {
        val materialButton3 = onView(
            allOf(
                withId(R.id.button_mark_tv_as_favorite),
                withText("Remove From Favorite"),
                childAtPosition(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        ts()
        val button = onView(
            allOf(
                withId(R.id.button_mark_tv_as_favorite),
                withText("Mark As Favorite"),
                withParent(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
        ts()
    }

    fun toFavoriteTv() {
        val materialButton = onView(
            allOf(
                withId(R.id.button_favorite_tvs), withText(R.string.tvs),
                childAtPosition(
                    allOf(
                        withId(R.id.fragment_favorite_movies_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        ts()
        val materialCardView3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.favorite_tv_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_favorites_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView3.perform(click())
        ts()
        val textView2 = onView(
            allOf(
                withId(R.id.tv_info_name),
                withParent(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))
        ts()
    }

    fun toFavoriteMovie() {
        val materialCardView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.favorite_movie_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_favorites_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView2.perform(click())
        ts()
        val textView = onView(
            allOf(
                withId(R.id.movie_info_title),
                withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    fun toWatchlist() {
        val navigationMenuItemView2 = onView(
            allOf(
                withId(R.id.menu_item_watchlists),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())
        ts()
    }

    fun toWatchlistedMovie() {
        val materialCardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.movie_watchlist_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_watchlist_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView.perform(click())
        ts()
    }

    fun removeMovieFromWatchlist() {
        val materialButton = onView(
            allOf(
                withId(R.id.button_watchlist), withText("Remove From Watchlist"),
                childAtPosition(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        ts()
    }

    fun addMovieToWatchlist() {
        val materialButton2 = onView(
            allOf(
                withId(R.id.button_watchlist), withText("Add To Watchlist"),
                childAtPosition(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        ts()
    }

    fun switchToWatchlistedTvs() {
        val materialButton3 = onView(
            allOf(
                withId(R.id.button_watchlist_tvs), withText("Tv's"),
                childAtPosition(
                    allOf(
                        withId(R.id.fragment_watchlist_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        ts()
    }

    fun toWatchlistedTv() {
        val materialCardView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.tv_watchlist_item_constraint_layout),
                        childAtPosition(
                            withId(R.id.fragment_watchlist_recycler_view),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialCardView2.perform(click())
        ts()
    }

    fun removeTvFromWatchlist() {
        val materialButton4 = onView(
            allOf(
                withId(R.id.button_watchlist), withText("Remove From Watchlist"),
                childAtPosition(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        ts()
    }

    fun addTvToWatchlist() {
        val materialButton5 = onView(
            allOf(
                withId(R.id.button_watchlist), withText("Add To Watchlist"),
                childAtPosition(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        childAtPosition(
                            withId(R.id.nav_host_container),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        ts()
    }

    fun checkRemoveMovieFromWatchlistBtn() {
        val button = onView(
            allOf(
                withId(R.id.button_watchlist), withText(R.string.remove_from_watchlist),
                withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
        ts()
    }

    fun checkAddMovieToWatchlistBtn() {
        val button = onView(
            allOf(
                withId(R.id.button_watchlist), withText(R.string.add_to_watchlist),
                withParent(
                    allOf(
                        withId(R.id.movie_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
        ts()
    }

    fun checkRemoveTvFromWatchlistBtn() {
        val button2 = onView(
            allOf(
                withId(R.id.button_watchlist), withText(R.string.remove_from_watchlist),
                withParent(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
        ts()
    }

    fun checkAddTvToWatchlistBtn() {
        val button2 = onView(
            allOf(
                withId(R.id.button_watchlist), withText(R.string.add_to_watchlist),
                withParent(
                    allOf(
                        withId(R.id.tv_info_constraint_layout),
                        withParent(withId(R.id.nav_host_container))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
        ts()
    }
}