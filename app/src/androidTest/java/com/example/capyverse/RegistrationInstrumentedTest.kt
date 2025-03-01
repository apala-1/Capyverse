package com.example.capyverse

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.capyverse.ui.activity.LoginActivity
import com.example.capyverse.ui.activity.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTest {
    @get:Rule
    val testRule = ActivityScenarioRule(SignUpActivity::class.java)
    @Test
    fun checkRegistration() {

        onView(withId(R.id.firstName)).perform(
            typeText("Annie")
        )

        onView(withId(R.id.lastName)).perform(
            typeText("Frank")
        )

        onView(withId(R.id.email)).perform(
            typeText("annie123@gmail.com")
        )

        onView(withId(R.id.password)).perform(
            typeText("Annie123")
        )

        closeSoftKeyboard()

        Thread.sleep(1500)

        onView(withId(R.id.signUpButton)).perform(
            click()
        )

        Thread.sleep(6000)
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()))
    }

}