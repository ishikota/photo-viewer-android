package com.ishikota.photoviewerandroid.ui.collectiondeatil

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ishikota.photoviewerandroid.data.repository.CollectionRepositoryImpl
import com.ishikota.photoviewerandroid.infra.paging.PagingListing

class CollectionDetailPagingRepository(private val useCase: CollectionDetailUseCase) {

    fun getListing(collectionId: String): PagingListing<CollectionDetailAdapter.Item> {
        val sourceFactory = CollectionDetailPageKeyedDataSource.Factory(collectionId, useCase)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(CollectionRepositoryImpl.PER_PAGE)
            .setPageSize(CollectionRepositoryImpl.PER_PAGE)
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
