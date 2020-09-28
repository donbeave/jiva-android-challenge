package com.zhokhov.jiva.challenge.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.zhokhov.jiva.challenge.data.http.ApiService
import com.zhokhov.jiva.challenge.data.http.AvatarRequest
import com.zhokhov.jiva.challenge.data.http.NewSessionRequest
import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import com.zhokhov.jiva.challenge.data.storage.Storage
import com.zhokhov.jiva.challenge.utils.Base64
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val storage: Storage,
    private val base64: Base64
) {

    fun getCredentials(): LoginCredentials? {
        return storage.getCredentials()
    }

    fun getSession(): LoginSession? {
        return storage.getSession()
    }

    fun login(email: String, password: String): Single<LoginSession> {
        Timber.d("Attempt to sign in for email %s", email)

        return apiService
            .initNewSession(NewSessionRequest(email, password))
            .map { session ->
                storage.saveEmailAndPassword(email, password)

                storage.saveUserIdAndToken(session.userId, session.token)
            }
    }

    fun getAvatar(): Single<Bitmap> {
        val session = storage.getSession()!!

        return apiService
            .getUserProfile("Bearer ${session.token}", session.userId)
            .doOnError { e ->
                // clear session if back-end was restarted and lost data
                if (e is HttpException) {
                    if (e.code() == 401) {
                        storage.clearSession()
                    }
                }
            }.map {
                getBitmapFromURL(it.avatarUrl)
            }
    }

    fun updateAvatar(bitmap: Bitmap): Single<String> {
        Timber.d("Uploading avatar")

        val session = storage.getSession()!!

        val outputStream = ByteArrayOutputStream()

        return outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it)

            val base64 = base64.encodeToString(it.toByteArray())

            apiService
                .uploadAvatar(
                    token = session.token,
                    userId = session.userId,
                    avatarRequest = AvatarRequest(base64)
                )
                .doOnError { e ->
                    // clear session if back-end was restarted and lost data
                    if (e is HttpException) {
                        if (e.code() == 401) {
                            storage.clearSession()
                        }
                    }
                }
                .map { response ->
                    response.avatarUrl
                }
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap {
        val url = URL(src)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        connection.inputStream.use { input ->
            return BitmapFactory.decodeStream(input)
        }
    }

}
