package com.zhokhov.jiva.challenge.ui.profile

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zhokhov.jiva.challenge.data.TestSchedulerProvider
import com.zhokhov.jiva.challenge.data.http.*
import com.zhokhov.jiva.challenge.data.repository.UserRepository
import com.zhokhov.jiva.challenge.data.storage.SimpleStorage
import com.zhokhov.jiva.challenge.utils.FakeBase64
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ProfileViewModelTest {

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
    fun `upload avatar`() {
        // given
        storage.saveUserIdAndToken("1", "123")
        storage.saveEmailAndPassword("xxx@xxx.xxx", "xxx")

        val profileViewModel = ProfileViewModel(userRepository, TestSchedulerProvider())

        val bitmap = Mockito.mock(Bitmap::class.java)

        // expect
        profileViewModel.updateAvatar(bitmap)
    }

}

private class TestApiService : ApiService {

    override fun initNewSession(newSessionRequest: NewSessionRequest): Single<NewSessionResponse> {
        return Single.just(NewSessionResponse("1", "123"))
    }

    override fun getUserProfile(token: String, userId: String): Single<EmailAndAvatarResponse> {
        return Single.just(
            EmailAndAvatarResponse(
                email = "xxx@xxx.xxx",
                avatarUrl = "http://test.com/1.jpg"
            )
        )
    }

    override fun uploadAvatar(
        token: String,
        userId: String,
        avatarRequest: AvatarRequest
    ): Single<AvatarResponse> {
        return Single.just(AvatarResponse(avatarUrl = "http://test.com/1.jpg"))
    }

}
