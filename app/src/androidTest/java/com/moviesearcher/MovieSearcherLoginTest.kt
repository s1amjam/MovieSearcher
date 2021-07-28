package com.moviesearcher


import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MovieSearcherLoginTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MovieSearcherActivity::class.java)

    @Test
    fun movieSearcherLoginTest() {
        val appCompatImageButton = onView(
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
        appCompatImageButton.perform(click())

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

        Thread.sleep(15000)
        onWebView()
            .withElement(
                findElement(
                    Locator.XPATH,
                    "//*[@id=\"main\"]/section/div/div/div[1]/a/h2"
                )
            )
            .perform(webClick())
        Thread.sleep(1000)
        onWebView()
            .withElement(findElement(Locator.ID, "username"))
            .perform(webClick())
            .perform(DriverAtoms.webKeys("testmyapp"))
            .withElement(findElement(Locator.ID, "password"))
            .perform(webClick())
            .perform(DriverAtoms.webKeys("testmyapp123"))
            .withElement(findElement(Locator.ID, "login_button"))
            .perform(webClick())
            .withElement(findElement(Locator.ID, "allow_authentication"))//deny_authentication
            .perform(webClick())
        Thread.sleep(2000)
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
        Thread.sleep(2000)
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
        Thread.sleep(2000)
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
