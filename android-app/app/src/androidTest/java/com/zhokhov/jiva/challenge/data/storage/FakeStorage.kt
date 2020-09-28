package com.zhokhov.jiva.challenge.data.storage

import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import javax.inject.Inject

class FakeStorage @Inject constructor() : Storage {

    override fun getCredentials(): LoginCredentials? {
        TODO("Not yet implemented")
    }

    override fun getSession(): LoginSession? {
        TODO("Not yet implemented")
    }

    override fun saveEmailAndPassword(email: String, password: String): LoginCredentials {
        TODO("Not yet implemented")
    }

    override fun saveUserIdAndToken(userId: String, token: String): LoginSession {
        TODO("Not yet implemented")
    }

    override fun clearSession() {
        TODO("Not yet implemented")
    }

}
