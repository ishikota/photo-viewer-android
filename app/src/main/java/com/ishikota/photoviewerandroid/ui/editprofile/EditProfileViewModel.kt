package com.ishikota.photoviewerandroid.ui.editprofile

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.data.repository.OauthTokenRepository
import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.infra.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val oauthTokenRepository: OauthTokenRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<Int>>()
    val errorMessage: LiveData<Event<Int>> = _errorMessage

    private val _updatedUser = MutableLiveData<User>()
    val updatedUser: LiveData<User> = _updatedUser

    private val _navigateAppRestartAction = MutableLiveData<Event<Unit>>()
    val navigateAppRestartAction: LiveData<Event<Unit>> = _navigateAppRestartAction

    @SuppressLint("CheckResult")
    fun updateProfile(firstName: String, lastName: String, location: String, bio: String) {
        userRepository.putMe(firstName, lastName, location, bio)
            .doOnSubscribe {
                _isLoading.postValue(true)
            }
            .doFinally {
                _isLoading.postValue(false)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _updatedUser.postValue(it)
            }, { error ->
                Timber.e(error)
                _errorMessage.value = Event(R.string.login_failure_message)
            })
    }

    fun logout() {
        oauthTokenRepository.logout()
        _navigateAppRestartAction.postValue(Event(Unit))
    }
}
