package com.ishikota.photoviewerandroid.ui.collectionlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class CollectionListViewModel<P> @Inject constructor(
    pagingRepository: CollectionListPagingRepository<P>
) : ViewModel() {
    private val params = MutableLiveData<P>()
    private val listing = Transformations.map(params) { pagingRepository.getCollections(it) }

    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    fun startLoading(loadParams: P) {
        params.postValue(loadParams)
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

    class Factory<P>(
        private val pagingRepository: CollectionListPagingRepository<P>
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(CollectionListViewModel::class.java)) {
                CollectionListViewModel(pagingRepository) as T
            } else {
                throw IllegalArgumentException("Unknown view model class.")
            }
    }
}
