package com.zhokhov.jiva.challenge.data.http

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class FakeApiService @Inject constructor() : ApiService {

    private val availableUsers = listOf(
        FakeUser("1", "test@test.com", "test", "http://localhost/image/original.jpg")
    )
    private val activeSessions = mutableListOf<NewSessionResponse>()

    private val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')

    override fun initNewSession(newSessionRequest: NewSessionRequest): Single<NewSessionResponse> {
        val fakeUser = availableUsers.find { it.email == newSessionRequest.email }

        if (fakeUser == null) {
            val response: Response<String> = Response.error(400, "user not found".toResponseBody())
            return Single.error(HttpException(response))
        }

        if (fakeUser.password != newSessionRequest.password) {
            val response: Response<String> = Response.error(401, "wrong password".toResponseBody())
            return Single.error(HttpException(response))
        }

        val session = NewSessionResponse(userId = fakeUser.id, token = randomID())

        activeSessions.add(session)

        return Single.just(session)
    }

    override fun getUserProfile(
        authHeader: String,
        userId: String
    ): Single<EmailAndAvatarResponse> {
        val session = activeSessions.find { it.token == getToken(authHeader) }

        if (session == null) {
            val response: Response<String> =
                Response.error(401, "no session was found".toResponseBody())
            return Single.error(HttpException(response))
        }

        val fakeUser = availableUsers.find { it.id == session.userId }

        if (fakeUser == null || fakeUser.id != userId) {
            val response: Response<String> = Response.error(401, "wrong user id".toResponseBody())
            return Single.error(HttpException(response))
        }

        return Single.just(
            EmailAndAvatarResponse(
                email = fakeUser.email,
                avatarUrl = fakeUser.avatarUrl
            )
        )
    }

    override fun uploadAvatar(
        authHeader: String,
        userId: String,
        avatarRequest: AvatarRequest
    ): Single<AvatarResponse> {
        val session = activeSessions.find { it.token == getToken(authHeader) }

        if (session == null) {
            val response: Response<String> =
                Response.error(401, "no session was found".toResponseBody())
            return Single.error(HttpException(response))
        }

        val fakeUser = availableUsers.find { it.id == session.userId }

        if (fakeUser == null || fakeUser.id != userId) {
            val response: Response<String> = Response.error(401, "wrong user id".toResponseBody())
            return Single.error(HttpException(response))
        }

        fakeUser.avatarUrl = "http://localhost/image/updated.jpg"

        return Single.just(
            AvatarResponse(
                avatarUrl = fakeUser.avatarUrl
            )
        )
    }

    private fun getToken(authHeader: String): String? {
        return authHeader.trim { it <= ' ' }.toLowerCase().replace("bearer ", "")
    }

    private fun randomID(): String = List(16) { chars.random() }.joinToString("").toLowerCase()

}
