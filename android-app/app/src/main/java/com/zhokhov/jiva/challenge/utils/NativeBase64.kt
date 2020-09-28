package com.zhokhov.jiva.challenge.utils

import javax.inject.Inject

class NativeBase64 @Inject constructor() : Base64 {

    override fun encodeToString(byteArray: ByteArray): String {
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_WRAP)
    }

}
