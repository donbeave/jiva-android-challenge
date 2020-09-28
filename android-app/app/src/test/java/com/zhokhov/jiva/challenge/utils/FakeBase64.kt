package com.zhokhov.jiva.challenge.utils

class FakeBase64 : Base64 {

    override fun encodeToString(byteArray: ByteArray): String {
        return "test"
    }

}
