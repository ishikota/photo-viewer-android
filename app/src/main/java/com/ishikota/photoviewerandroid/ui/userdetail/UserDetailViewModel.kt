package com.ishikota.photoviewerandroid.ui.userdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.data.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userDetail = MutableLiveData<User>()
    val userDetail: LiveData<User> = _userDetail

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadUserDetail(username: String) {
        userRepository.getUser(username)
            .doOnSubscribe {
                _isError.postValue(false)
                _isLoading.postValue(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ collection ->
                _userDetail.postValue(collection)
                _isLoading.postValue(false)
            }, { error ->
                Timber.e(error)
                _isLoading.postValue(false)
                _isError.postValue(true)
            }).also { compositeDisposable.add(it) }
    }
}
