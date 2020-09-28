package com.zhokhov.jiva.challenge.ui.profile

import android.graphics.Bitmap

sealed class AvatarState
data class AvatarSuccess(val avatar: Bitmap) : AvatarState()
data class AvatarError(val error: Int) : AvatarState()
