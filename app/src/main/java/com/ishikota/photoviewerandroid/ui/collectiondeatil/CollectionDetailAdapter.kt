package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.databinding.CollectiondetailCarouselViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PagingNetworkStateViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotolistPhotoViewHolderBinding
import com.ishikota.photoviewerandroid.infra.fitViewSizeToPhoto
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkStateViewHolder

class CollectionDetailAdapter(
    private val onPhotoClicked: (Photo) -> Unit,
    private val onRelatedCollectionClicked: (Collection) -> Unit,
    private val retryCallback: () -> Unit
) : PagedListAdapter<CollectionDetailAdapter.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkState: PagingNetworkState? = null

    override fun getItemViewType(position: Int): Int =
        if (hasExtraRow() && position == itemCount - 1) {
            R.layout.paging_network_state_view_holder
        } else {
            when (getItem(position)) {
                is Item.PhotoItem -> R.layout.photolist_photo_view_holder
                is Item.RelatedCollections -> R.layout.collectiondetail_carousel_view_holder
                else -> throw IllegalArgumentException(
                    "unexpected item. item=${getItem(position)}"
                )
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
            R.layout.photolist_photo_view_holder -> PhotoViewHolder(
                PhotolistPhotoViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onPhotoClicked
            )
            R.layout.collectiondetail_carousel_view_holder -> CarouselViewHolder(
                CollectiondetailCarouselViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onRelatedCollectionClicked
            )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PagingNetworkStateViewHolder) {
            holder.bindTo(networkState)
        } else {
            val item = getItem(position)
            when {
                holder is PhotoViewHolder && item is Item.PhotoItem -> holder.bind(item)
                holder is CarouselViewHolder && item is Item.RelatedCollections -> holder.bind(item)
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

    sealed class Item {
        data class PhotoItem(val entity: Photo) : Item()
        data class RelatedCollections(val collections: List<Collection>) : Item()
    }

    private class PhotoViewHolder(
        private val binding: PhotolistPhotoViewHolderBinding,
        private val onPhotoClicked: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item.PhotoItem) {
            val viewWidth = getScreenWidth(binding.root.context)
            binding.photoImage.fitViewSizeToPhoto(viewWidth, item.entity)
            binding.photoImage.setOnClickListener {
                onPhotoClicked(item.entity)
            }

            binding.photo = item.entity
            binding.executePendingBindings()
        }

        private fun getScreenWidth(context: Context): Int {
            val metrics = DisplayMetrics()
            DisplayManagerCompat.getInstance(context).getDisplay(Display.DEFAULT_DISPLAY)
                ?.getMetrics(metrics)
            return metrics.widthPixels
        }
    }

    private class CarouselViewHolder(
        private val binding: CollectiondetailCarouselViewHolderBinding,
        private val onRelatedCollectionClicked: (Collection) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item.RelatedCollections) {
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            binding.recyclerView.adapter = CollectionDetailCarouselAdapter(item, onRelatedCollectionClicked)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = when {
                oldItem is Item.PhotoItem && newItem is Item.PhotoItem ->
                    oldItem.entity.id == newItem.entity.id
                else -> false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
