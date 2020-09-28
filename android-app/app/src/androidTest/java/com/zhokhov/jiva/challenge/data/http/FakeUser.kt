package com.zhokhov.jiva.challenge.data.http

data class FakeUser(
    val id: String,
    val email: String,
    val password: String,
    var avatarUrl: String
) {
}
