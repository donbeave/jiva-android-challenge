package com.zhokhov.jiva.challenge.ui.profile

import android.graphics.Bitmap
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhokhov.jiva.challenge.data.model.LoginCredentials
import com.zhokhov.jiva.challenge.data.repository.UserRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber

class ProfileViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginCredentials = MutableLiveData<LoginCredentials>().also { thisLiveData ->
        userRepository.getCredentials()?.let {
            thisLiveData.value = it
        }
    }
    val loginCredentials: LiveData<LoginCredentials> = _loginCredentials

    private val _avatarState = MutableLiveData<AvatarState>()
    val avatarState: LiveData<AvatarState> = _avatarState

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun loadAvatar() {
        disposables.add(
            userRepository
                .getAvatar()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Bitmap>() {
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.code() == 401) {
                                _avatarState.value = AvatarError(0)
                            }
                        }
                    }

                    override fun onSuccess(value: Bitmap) {
                        Timber.d("Successfully loaded avatar")

                        _avatarState.value = AvatarSuccess(value)
                    }
                })
        )
    }

    fun updateAvatar(bitmap: Bitmap) {
        disposables.add(
            userRepository
                .updateAvatar(bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<String>() {
                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.code() == 401) {
                                _avatarState.value = AvatarError(0)
                            }
                        }
                    }

                    override fun onSuccess(value: String) {
                        Timber.d("Successfully uploaded avatar")

                        // reload avatar
                        loadAvatar()
                    }
                })
        )
    }

}
