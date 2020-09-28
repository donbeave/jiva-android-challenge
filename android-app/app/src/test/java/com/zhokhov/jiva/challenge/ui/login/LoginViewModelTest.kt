package com.zhokhov.jiva.challenge.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.zhokhov.jiva.challenge.data.TestSchedulerProvider
import com.zhokhov.jiva.challenge.data.http.*
import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.model.LoginSession
import com.zhokhov.jiva.challenge.data.repository.UserRepository
import com.zhokhov.jiva.challenge.data.storage.SimpleStorage
import com.zhokhov.jiva.challenge.data.storage.Storage
import com.zhokhov.jiva.challenge.utils.FakeBase64
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import timber.log.Timber
import javax.inject.Inject

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var apiService: TestApiService
    private lateinit var storage: SimpleStorage
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        apiService = TestApiService()
        storage = SimpleStorage()
        userRepository = UserRepository(apiService, storage, FakeBase64())
    }

    @Test
    fun `initial state`() {
        // given
        val loginViewModel = LoginViewModel(userRepository, TestSchedulerProvider())

        // expect
        assertThat(loginViewModel.loginState.value).isNull()
        assertThat(loginViewModel.loginCredentials.value).isNull()
    }

    @Test
    fun `restore logged in state`() {
        // given
        storage.saveUserIdAndToken("1", "123")
        storage.saveEmailAndPassword("xxx@xxx.xxx", "xxx")

        val loginViewModel = LoginViewModel(userRepository, TestSchedulerProvider())

        // expect
        assertThat(loginViewModel.loginState.value).isEqualTo(LoginSuccess(session = storage.getSession()!!))
        assertThat(loginViewModel.loginCredentials.value).isEqualTo(storage.getCredentials())
    }

    @Test
    fun `login`() {
        // given
        val email = "abc@abc.com"
        val password = "abc"
        val loginViewModel = LoginViewModel(userRepository, TestSchedulerProvider())

        // when
        loginViewModel.login(email, password)

        // then
        assertThat(storage.getSession()).isEqualTo(LoginSession(userId = "1", token = "123"))
        assertThat(storage.getCredentials()).isEqualTo(
            LoginCredentials(
                email = email,
                password = password
            )
        )

        assertThat(loginViewModel.loginState.value).isEqualTo(LoginSuccess(session = storage.getSession()!!))
    }

}

private class TestApiService : ApiService {

    override fun initNewSession(newSessionRequest: NewSessionRequest): Single<NewSessionResponse> {
        return Single.just(NewSessionResponse("1", "123"))
    }

    override fun getUserProfile(token: String, userId: String): Single<EmailAndAvatarResponse> {
        throw NotImplementedError("Not used in this test")
    }

    override fun uploadAvatar(
        token: String,
        userId: String,
        avatarRequest: AvatarRequest
    ): Single<AvatarResponse> {
        throw NotImplementedError("Not used in this test")
    }

}
