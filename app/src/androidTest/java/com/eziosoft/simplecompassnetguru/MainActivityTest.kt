/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/14/21 10:26 PM
 */

package com.eziosoft.simplecompassnetguru

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.eziosoft.simplecompassnetguru.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isMainFragmentInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.mainFragmentLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isTargetFragmentInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.set_destination_button)).perform(click())
        onView(withId(R.id.targetInputFragmentLayout)).check(matches(isDisplayed()))
    }

}