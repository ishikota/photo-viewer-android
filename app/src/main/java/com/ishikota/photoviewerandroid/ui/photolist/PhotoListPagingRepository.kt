package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.data.repository.PhotoRepositoryImpl
import com.ishikota.photoviewerandroid.infra.paging.PagingListing

class PhotoListPagingRepository(private val photoRepository: PhotoRepository) {

    fun getPhotos(listOrder: PhotoRepository.Order): PagingListing<Photo> {
        val sourceFactory = PhotoListPageKeyedDataSource.Factory(listOrder, photoRepository)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PhotoRepositoryImpl.PER_PAGE)
            .setPageSize(PhotoRepositoryImpl.PER_PAGE)
            .build()
        val livePagedList = LivePagedListBuilder(sourceFactory, config).build()
        return PagingListing(
            pagedList = livePagedList,
            initialLoadNetworkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.initialLoad },
            loadMoreNetworkState = Transformations.switchMap(sourceFactory.sourceLiveData) { it.networkState },
            retry = { sourceFactory.sourceLiveData.value?.retry() },
            refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
            clear = { sourceFactory.sourceLiveData.value?.clear() }
        )
    }
}
