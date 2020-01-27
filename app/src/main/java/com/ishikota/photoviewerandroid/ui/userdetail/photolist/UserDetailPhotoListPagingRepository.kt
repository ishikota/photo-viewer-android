package com.ishikota.photoviewerandroid.ui.userdetail.photolist

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ishikota.photoviewerandroid.data.repository.PhotoRepositoryImpl
import com.ishikota.photoviewerandroid.infra.paging.PagingListing
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter

class UserDetailPhotoListPagingRepository(private val userCase: UserDetailPhotoListUseCase) {

    fun getPhotos(userName: String): PagingListing<PhotoListAdapter.Item> {
        val sourceFactory =
            UserDetailPhotoListPageKeyedDataSource.Factory(
                userName,
                userCase
            )
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
