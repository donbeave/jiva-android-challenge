package com.zhokhov.jiva.challenge.data.http

import com.fasterxml.jackson.annotation.JsonProperty

data class NewSessionResponse(
    @JsonProperty("userid") val userId: String, val token: String
)
