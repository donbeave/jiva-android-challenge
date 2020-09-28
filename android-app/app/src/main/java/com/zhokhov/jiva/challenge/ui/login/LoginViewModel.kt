package com.zhokhov.jiva.challenge.ui.login

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhokhov.jiva.challenge.R
import com.zhokhov.jiva.challenge.data.SchedulerProvider
import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import com.zhokhov.jiva.challenge.data.repository.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    companion object {
        const val PASSWORD_MIN_LENGTH = 4
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginCredentials = MutableLiveData<LoginCredentials>().also { thisLiveData ->
        // auto-fill credentials in login form
        userRepository.getCredentials()?.let {
            thisLiveData.value = it
        }
    }
    val loginCredentials: LiveData<LoginCredentials> = _loginCredentials

    private val _loginState = MutableLiveData<LoginState>().also { thisLiveData ->
        // auto-login if session already exists
        userRepository.getSession()?.let {
            thisLiveData.value = LoginSuccess(it)
        }
    }
    val loginState: LiveData<LoginState> = _loginState

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun login(email: String, password: String) {
        disposables.add(
            userRepository
                .login(email, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : DisposableSingleObserver<LoginSession>() {
                    override fun onError(e: Throwable) {
                        Timber.e(e, "Error during login")

                        if (e is HttpException) {
                            if (e.code() == 400) {
                                _loginState.value = LoginError(R.string.user_not_found)
                            } else if (e.code() == 401) {
                                _loginState.value = LoginError(R.string.wrong_password)
                            }
                        } else {
                            _loginState.value = LoginError(R.string.unexpected_error)
                        }
                    }

                    override fun onSuccess(value: LoginSession) {
                        Timber.d("Successfully logged in")

                        _loginState.value = LoginSuccess(value)
                    }
                })
        )
    }

    fun credentialsChanged(email: String, password: String) {
        if (email.isNotBlank() || password.isNotBlank()) {
            val emailError =
                if (email.isNotBlank() && !isEmailValid(email)) R.string.invalid_email_format else null
            val passwordError =
                if (password.isNotBlank() && !isPasswordValid(password)) R.string.invalid_password_format else null

            _loginForm.value = LoginFormState(
                emailError = emailError,
                passwordError = passwordError,
                isDataValid = emailError == null && passwordError == null
            )
        } else {
            _loginForm.value = LoginFormState(isDataValid = false)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@") && email.contains(".")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= PASSWORD_MIN_LENGTH
    }

}
