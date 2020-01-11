package com.ishikota.photoviewerandroid.ui.collectionlist

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.databinding.CollectionlistCollectionViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PagingNetworkStateViewHolderBinding
import com.ishikota.photoviewerandroid.infra.fitViewSizeToPhoto
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkStateViewHolder

class CollectionListAdapter(
    private val retryCallback: () -> Unit,
    private val onCollectionClicked: (Collection) -> Unit,
    private val onUserClicked: (User) -> Unit
) : PagedListAdapter<Collection, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkState: PagingNetworkState? = null

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.paging_network_state_view_holder
        } else {
            R.layout.collectionlist_collection_view_holder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.paging_network_state_view_holder ->
                PagingNetworkStateViewHolder(
                    PagingNetworkStateViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    retryCallback
                )
            R.layout.collectionlist_collection_view_holder ->
                CollectionViewHolder(
                    CollectionlistCollectionViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onCollectionClicked,
                    onUserClicked
                )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PagingNetworkStateViewHolder) {
            holder.bindTo(networkState)
        } else {
            val item = getItem(position)
            when {
                holder is CollectionViewHolder && item != null -> holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: PagingNetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != PagingNetworkState.LOADED

    private class CollectionViewHolder(
        private val binding: CollectionlistCollectionViewHolderBinding,
        private val onCollectionClicked: (Collection) -> Unit,
        private val onUserClicked: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(collection: Collection) {
            val viewWidth = getScreenWidth(binding.root.context)
            binding.coverImage.fitViewSizeToPhoto(viewWidth, collection.coverPhoto)
            binding.root.requestLayout()

            binding.root.setOnClickListener {
                onCollectionClicked(collection)
            }
            binding.userThumbnail.setOnClickListener {
                onUserClicked(collection.user)
            }
            binding.collection = collection
            binding.executePendingBindings()
        }

        private fun getScreenWidth(context: Context): Int {
            val metrics = DisplayMetrics()
            DisplayManagerCompat.getInstance(context).getDisplay(Display.DEFAULT_DISPLAY)
                ?.getMetrics(metrics)
            return metrics.widthPixels
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Collection>() {
            override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean =
                oldItem == newItem
        }
    }
}
