package com.zhokhov.jiva.challenge

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.zhokhov.jiva.challenge.R.*
import com.zhokhov.jiva.challenge.di.StorageModule
import com.zhokhov.jiva.challenge.ui.login.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@UninstallModules(StorageModule::class)
@HiltAndroidTest
class JivaChallengeApplicationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun runApp() {
        ActivityScenario.launch(LoginActivity::class.java)

        // Start from Login View
        Espresso.onView(withId(id.emailInputBox))
            .perform(ViewActions.typeText("test@test.com"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(id.passwordInputBox))
            .perform(ViewActions.typeText("test"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(id.loginButton)).perform(ViewActions.click())
    }

}