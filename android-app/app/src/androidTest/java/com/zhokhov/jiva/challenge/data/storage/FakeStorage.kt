package com.zhokhov.jiva.challenge.data.storage

import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.log

class FakeStorage @Inject constructor() : Storage {

    private var loginCredentials: LoginCredentials? = null
    private var loginSession: LoginSession? = null

    override fun getCredentials(): LoginCredentials? {
        return loginCredentials
    }

    override fun getSession(): LoginSession? {
        return loginSession
    }

    override fun saveEmailAndPassword(email: String, password: String): LoginCredentials {
        Timber.d("Saving credentials: email = %s, password = %s", email, password)

        loginCredentials = LoginCredentials(email, password)
        return loginCredentials!!
    }

    override fun saveUserIdAndToken(userId: String, token: String): LoginSession {
        Timber.d("Saving session: userId = %s, token = %s", userId, token)

        loginSession = LoginSession(userId, token)
        return loginSession!!
    }

    override fun clearSession() {
        loginSession = null
    }

}
