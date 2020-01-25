package com.ishikota.photoviewerandroid.ui.userdetail.photolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class UserDetailPhotoListViewModel @Inject constructor(
    private val pagingRepository: UserDetailPhotoListPagingRepository
) : ViewModel() {
    private val userName = MutableLiveData<String>()
    private val listing = Transformations.map(userName) { pagingRepository.getPhotos(it) }

    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    fun setUserName(userName: String) {
        this.userName.postValue(userName)
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun retry() {
        listing.value?.retry?.invoke()
    }

    override fun onCleared() {
        listing.value?.clear?.invoke()
    }
}
