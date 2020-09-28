package com.zhokhov.jiva.challenge.data.storage

import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession

interface Storage {

    fun getCredentials(): LoginCredentials?

    fun getSession(): LoginSession?

    fun saveEmailAndPassword(email: String, password: String): LoginCredentials

    fun saveUserIdAndToken(userId: String, token: String): LoginSession

    fun clearSession()

    fun clearAll()

}
