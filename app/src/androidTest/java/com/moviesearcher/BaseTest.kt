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
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
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
                if (Web.onWebView()
                        .withElement(
                            DriverAtoms.findElement(
                                Locator.ID,
                                "allow_authentication"
                            )
                        ) != null
                ) {
                    Web.onWebView().withElement(
                        DriverAtoms.findElement(
                            Locator.ID,
                            "allow_authentication"
                        )
                    ).perform(DriverAtoms.webClick())
                    ts()
                } else {
                    if (Web.onWebView()
                            .withElement(
                                DriverAtoms.findElement(
                                    Locator.XPATH,
                                    "//*[@id=\"main\"]/section/div/div/div[1]/a/h2"
                                )
                            ) != null
                    ) {
                        Web.onWebView()
                            .withElement(
                                DriverAtoms.findElement(
                                    Locator.XPATH,
                                    "//*[@id=\"main\"]/section/div/div/div[1]/a/h2"
                                )
                            )
                            .perform(DriverAtoms.webClick())
                        ts()
                        Web.onWebView()
                            .withElement(DriverAtoms.findElement(Locator.ID, "username"))
                            .perform(DriverAtoms.webClick())
                            .perform(DriverAtoms.webKeys("testmyapp"))
                            .withElement(DriverAtoms.findElement(Locator.ID, "password"))
                            .perform(DriverAtoms.webClick())
                            .perform(DriverAtoms.webKeys("testmyapp123"))
                            .withElement(DriverAtoms.findElement(Locator.ID, "login_button"))
                            .perform(DriverAtoms.webClick())
                            .withElement(
                                DriverAtoms.findElement(
                                    Locator.ID,
                                    "allow_authentication"
                                )
                            )//deny_authentication
                            .perform(DriverAtoms.webClick())
                        ts()
                    }
                }
            } else {
                logout()
                login()
            }
        }

        @JvmStatic
        fun logout() {
            launchActivity()
            openNavDrawer()

            val asd = onView(
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

            if (asd != null) {
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
        ts()
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
    }

    fun markMovieAsFavorite() {
        ts()
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
    }

    fun markTvAsFavorite() {
        ts()
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
    }

    fun removeMovieFromFavorite() {
        ts()
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
    }

    fun removeTvFromFavorite() {
        ts()
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
    }

    fun toFavoriteTv() {
        ts()
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
    }

    fun toFavoriteMovie() {
        ts()
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
}