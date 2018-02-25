package ca.nick.basicsamplemyversion.ui

import android.arch.core.executor.testing.CountingTaskExecutorRule
import android.arch.lifecycle.Observer
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.widget.RecyclerView
import ca.nick.basicsamplemyversion.AppExecutors
import ca.nick.basicsamplemyversion.EspressoTestUtil
import ca.nick.basicsamplemyversion.R
import ca.nick.basicsamplemyversion.db.AppDatabase
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MainActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    init {
        InstrumentationRegistry.getContext().deleteDatabase(AppDatabase.DATABASE_NAME)
    }

    @Before
    fun disableRecyclerViewAnimations() {
        EspressoTestUtil.disableAnimations(activityTestRule)
    }

    @Before
    fun waitForDbCreation() {
        val latch = CountDownLatch(1)
        val databaseCreated = AppDatabase.getInstance(
            InstrumentationRegistry.getTargetContext(), AppExecutors()
        )
            .isDatabaseCreated

        activityTestRule.runOnUiThread(Runnable {
            databaseCreated.observeForever(object : Observer<Boolean> {
                override fun onChanged(aBoolean: Boolean?) {
                    if (java.lang.Boolean.TRUE == aBoolean) {
                        databaseCreated.removeObserver(this)
                        latch.countDown()
                    }
                }
            })
        })

        MatcherAssert.assertThat(
            "database should've initialized",
            latch.await(1, TimeUnit.MINUTES), CoreMatchers.`is`(true)
        )
    }

    @Test
    fun clickOnFirstItem_opensComments() {
        drain()

        onView(withContentDescription(R.string.cd_products_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        drain()

        onView(withContentDescription(R.string.cd_comments_list))
            .check(matches(isDisplayed()))

        drain()

        onView(withContentDescription(R.string.cd_product_name))
            .check(matches(not(withText(""))))
    }

    private fun drain(mins: Int = 1) {
        countingTaskExecutorRule.drainTasks(mins, TimeUnit.MINUTES)
    }
}