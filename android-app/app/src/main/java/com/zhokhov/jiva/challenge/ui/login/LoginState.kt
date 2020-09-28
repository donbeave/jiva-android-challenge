package com.zhokhov.jiva.challenge.ui.login

import com.zhokhov.jiva.challenge.data.model.LoginSession

sealed class LoginState
data class LoginSuccess(val session: LoginSession) : LoginState()
data class LoginError(val error: Int) : LoginState()
