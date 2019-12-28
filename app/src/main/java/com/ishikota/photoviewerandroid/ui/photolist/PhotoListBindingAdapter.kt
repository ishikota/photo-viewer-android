package com.ishikota.photoviewerandroid.ui.photolist

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status


@BindingAdapter("hideProgress")
fun SwipeRefreshLayout.hideProgress(networkState: PagingNetworkState?) {
    if (networkState != null && networkState.status != Status.RUNNING) {
        isRefreshing = false
    }
}
