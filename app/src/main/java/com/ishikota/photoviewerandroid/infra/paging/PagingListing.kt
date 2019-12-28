package com.ishikota.photoviewerandroid.infra.paging

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingListing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status of first page loading to show to the user
    val initialLoadNetworkState: LiveData<PagingNetworkState>,
    // represents the network request status of loading extra pages to show to the user
    val loadMoreNetworkState: LiveData<PagingNetworkState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit,
    // clean up resources.
    val clear: () -> Unit
)
