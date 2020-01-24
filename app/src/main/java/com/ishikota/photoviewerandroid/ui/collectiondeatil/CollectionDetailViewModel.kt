package com.ishikota.photoviewerandroid.ui.collectiondeatil

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CollectionDetailViewModel @Inject constructor(
    private val pagingRepository: CollectionDetailPagingRepository
) : ViewModel() {

    private val collectionId = MutableLiveData<String>()
    private val listing = Transformations.map(collectionId) { pagingRepository.getListing(it) }

    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    fun setCollectionId(id: String) {
        collectionId.postValue(id)
    }

    fun retry() {
        listing.value?.retry?.invoke()
    }

    override fun onCleared() {
        listing.value?.clear?.invoke()
    }
}
