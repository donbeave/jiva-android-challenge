package com.zhokhov.jiva.challenge

import android.graphics.Bitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.android.material.textfield.TextInputEditText
import com.google.common.truth.Truth.assertThat
import com.zhokhov.jiva.challenge.R.id
import com.zhokhov.jiva.challenge.di.NetworkModule
import com.zhokhov.jiva.challenge.di.StorageModule
import com.zhokhov.jiva.challenge.ui.login.LoginActivity
import com.zhokhov.jiva.challenge.ui.profile.ProfileActivity
import com.zhokhov.jiva.challenge.utils.EspressoHelper.getCurrentActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(StorageModule::class, NetworkModule::class)
class JivaChallengeApplicationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun login() {
        // open the app (start from login activity)
        ActivityScenario.launch(LoginActivity::class.java)

        // fill email and password and login
        onView(withId(id.emailInputBox)).perform(
            typeText("test@test.com"),
            closeSoftKeyboard()
        )
        onView(withId(id.passwordInputBox)).perform(
            typeText("test"),
            closeSoftKeyboard()
        )
        onView(withId(id.loginButton)).perform(click())

        // check profile view was opened
        val activity = getCurrentActivity()!!

        assertThat(activity.javaClass).isEqualTo(ProfileActivity::class.java)

        // check email and password boxes
        val profileEmailBox = activity.findViewById<TextInputEditText>(id.profileEmailBox)

        assertThat(profileEmailBox.text.toString()).isEqualTo("test@test.com")

        val profilePasswordBox = activity.findViewById<TextInputEditText>(id.profilePasswordBox)

        assertThat(profilePasswordBox.text.toString()).isEqualTo("test")

        // upload avatar
        runOnUiThread({
            val bitmap = Bitmap.createBitmap(createColors(100), 10, 10, Bitmap.Config.RGB_565)

            (activity as ProfileActivity).updateAvatar(bitmap)
        })
    }

    private fun createColors(size: Int): IntArray {
        val colors = IntArray(size)
        for (i in 0 until size) {
            colors[i] = 0xFF shl 24 or (i shl 16) or (i shl 8) or i
        }
        return colors
    }

}
