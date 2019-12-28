package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.infra.paging.PagingListing

class PhotoListViewModel(
    pagingRepository: PhotoListPagingRepository
) : ViewModel() {

    private val listing =
        MutableLiveData<PagingListing<Photo>>(pagingRepository.getPhotos(PhotoRepository.Order.POPULAR))

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
        private val pagingRepository: PhotoListPagingRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(PhotoListViewModel::class.java)) {
                PhotoListViewModel(pagingRepository) as T
            } else {
                throw IllegalArgumentException("Unexpected modelClass=$modelClass.")
            }
    }
}
