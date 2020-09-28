package com.zhokhov.jiva.challenge.data.http

import com.fasterxml.jackson.annotation.JsonProperty

data class EmailAndAvatarResponse(
    val email: String,
    @JsonProperty("avatar_url") val avatarUrl: String
)
