package com.ishikota.photoviewerandroid.ui.collectionlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.infra.paging.PagingListing

class CollectionListViewModel(
    pagingRepository: CollectionListPagingRepository
) : ViewModel() {
    private val listing =
        MutableLiveData<PagingListing<Collection>>(pagingRepository.getCollections())

    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun retry() {
        listing.value?.retry?.invoke()
    }

    override fun onCleared() {
        listing.value?.clear?.invoke()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: CollectionListPagingRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(CollectionListViewModel::class.java)) {
                CollectionListViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unexpected modelClass=$modelClass.")
            }
    }
}
