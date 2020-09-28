package com.zhokhov.jiva.challenge.data.http

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ApiService {

    @POST("/sessions/new")
    fun initNewSession(@Body newSessionRequest: NewSessionRequest): Single<NewSessionResponse>

    @GET("/users/{userId}")
    fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Single<EmailAndAvatarResponse>

    @POST("/users/{userId}/avatar")
    fun uploadAvatar(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body avatarRequest: AvatarRequest
    ): Single<AvatarResponse>

}
