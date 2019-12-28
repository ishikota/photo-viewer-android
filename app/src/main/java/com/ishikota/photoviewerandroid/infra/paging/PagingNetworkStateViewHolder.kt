package com.ishikota.photoviewerandroid.infra.paging

import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.databinding.PagingNetworkStateViewHolderBinding

class PagingNetworkStateViewHolder(
    private val binding: PagingNetworkStateViewHolderBinding,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(networkState: PagingNetworkState?) {
        binding.networkState = networkState
        binding.executePendingBindings()
    }
}

