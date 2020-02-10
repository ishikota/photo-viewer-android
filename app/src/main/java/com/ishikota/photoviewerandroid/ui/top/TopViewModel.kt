package com.ishikota.photoviewerandroid.ui.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.data.repository.OauthTokenRepository
import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.infra.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TopViewModel @Inject constructor(
    private val oauthTokenRepository: OauthTokenRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _navigateToMeAction = MutableLiveData<Event<User>>()
    val navigateToMeAction: LiveData<Event<User>> = _navigateToMeAction

    private val _navigateToLogin = MutableLiveData<Event<Unit>>()
    val navigateToLogin: LiveData<Event<Unit>> = _navigateToLogin

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<Int>>()
    val errorMessage: LiveData<Event<Int>> = _errorMessage

    private val compositeDisposable = CompositeDisposable()

    fun requestAccountMenu() {
        if (oauthTokenRepository.isLoggedIn()) {
            userRepository.getMe()
                .doOnSubscribe {
                    _isLoading.postValue(true)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ me ->
                    _navigateToMeAction.value = Event(me)
                    _isLoading.postValue(false)
                }, { error ->
                    Timber.e(error)
                    _isLoading.postValue(false)
                    _errorMessage.postValue(Event(R.string.account_fetch_failure))
                }).also { compositeDisposable.add(it) }
        } else {
            _navigateToLogin.value = Event(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
