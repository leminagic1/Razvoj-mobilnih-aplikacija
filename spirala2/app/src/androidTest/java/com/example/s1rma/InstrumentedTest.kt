package com.example.s1rma


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasCategories
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.util.regex.Matcher
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers


@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentedTest {
    @get:Rule
    val activityRule = IntentsTestRule(NovaBiljkaActivity::class.java)

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.CAMERA)
    @Test
    fun testDodajIstoJelo() {
        val activity = activityRule.activity

        activity.runOnUiThread {
            val jelaListView = activity.getJelaListView()
            val jelaAdapter = jelaListView.adapter as JelaAdapter


            jelaAdapter.dodajJelo("Grah")
            val isGrahAdded = jelaAdapter.getAllJela().contains("Grah")

            jelaAdapter.dodajJelo("grah")
            val isGrahAddedAgain = jelaAdapter.getAllJela().contains("Grah")


            assertTrue(
                "Dva ista jela s različitim slučajem slova nisu dodana u listu",
                isGrahAdded && isGrahAddedAgain
            )
        }

        }


    @Test
    fun testDisplayImage() {

        val imageBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val resultIntent = Intent().apply {
            putExtra("data", imageBitmap)
        }


        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                resultIntent
            )
        )


        onView(withId(R.id.slikaIV)).perform(scrollTo())


        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())


        onView(withId(R.id.slikaIV)).check(matches(isDisplayed()))

    }
}

