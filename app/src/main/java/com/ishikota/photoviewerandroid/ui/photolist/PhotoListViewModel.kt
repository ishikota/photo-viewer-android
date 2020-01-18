package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import javax.inject.Inject

class PhotoListViewModel @Inject constructor(
    private val pagingRepository: PhotoListPagingRepository
) : ViewModel() {
    private val listOrder = MutableLiveData<PhotoRepository.Order>(DEFAULT_ORDER)
    private val listing = Transformations.map(listOrder) { pagingRepository.getPhotos(it) }

    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    fun updateListOrder(order: PhotoRepository.Order) {
        listOrder.postValue(order)
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

    companion object {
        private val DEFAULT_ORDER = PhotoRepository.Order.POPULAR
    }
}
