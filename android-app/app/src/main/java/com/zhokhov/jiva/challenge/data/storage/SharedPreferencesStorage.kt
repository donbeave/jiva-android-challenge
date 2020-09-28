package com.zhokhov.jiva.challenge.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class SharedPreferencesStorage @Inject constructor(@ApplicationContext appContext: Context) :
    Storage {

    private val emailKey = "ACCOUNT_EMAIL"
    private val passwordKey = "ACCOUNT_PASSWORD"
    private val userIdKey = "ACCOUNT_USER_ID"
    private val tokenKey = "ACCOUNT_TOKEN"

    private val sharedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            appContext,
            "jiva_settings",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun getCredentials(): LoginCredentials? {
        val email = sharedPreferences.getString(emailKey, null)
        val password = sharedPreferences.getString(passwordKey, null)

        if (email == null || password == null) {
            return null
        }

        return LoginCredentials(email, password)
    }

    override fun getSession(): LoginSession? {
        val userId = sharedPreferences.getString(userIdKey, null)
        val token = sharedPreferences.getString(tokenKey, null)

        if (userId == null || token == null) {
            return null
        }

        return LoginSession(userId, token)
    }

    override fun saveEmailAndPassword(email: String, password: String): LoginCredentials {
        with(sharedPreferences.edit()) {
            putString(emailKey, email)
            putString(passwordKey, password)
            commit()
        }

        return LoginCredentials(email, password)
    }

    override fun saveUserIdAndToken(userId: String, token: String): LoginSession {
        with(sharedPreferences.edit()) {
            putString(userIdKey, userId)
            putString(tokenKey, token)
            commit()
        }

        return LoginSession(userId, token)
    }

    override fun clearSession() {
        with(sharedPreferences.edit()) {
            remove(userIdKey)
            remove(tokenKey)
            commit()
        }
    }

}
