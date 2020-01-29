package com.ishikota.photoviewerandroid.ui.photolist

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.databinding.PagingNetworkStateViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotolistFilterViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotolistPhotoViewHolderBinding
import com.ishikota.photoviewerandroid.infra.fitViewSizeToPhoto
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkStateViewHolder


class PhotoListAdapter(
    private val retryCallback: () -> Unit,
    private val onPhotoClicked: (Photo) -> Unit,
    private val onOrderChangeRequested: (View) -> Unit,
    private val onGridChangeRequested: (View) -> Unit
) : PagedListAdapter<PhotoListAdapter.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    // Use LinearLayoutManager in default
    var isGridMode = false

    fun updateLayoutManager(recyclerView: RecyclerView, isGridMode: Boolean) {
        val layoutManager = if (isGridMode) {
            GridLayoutManager(recyclerView.context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        when (getItemViewType(position)) {
                            R.layout.photolist_filter_view_holder,
                            R.layout.paging_network_state_view_holder -> 2
                            else -> 1
                        }
                }
            }
        } else {
            LinearLayoutManager(recyclerView.context)
        }
        this.isGridMode = isGridMode
        recyclerView.layoutManager = layoutManager
    }

    sealed class Item {
        data class Header(
            val currentOrder: PhotoRepository.Order
        ) : Item()

        data class PhotoItem(
            val entity: Photo
        ) : Item()
    }

    private var networkState: PagingNetworkState? = null

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.paging_network_state_view_holder
        } else {
            when (getItem(position)) {
                is Item.Header -> R.layout.photolist_filter_view_holder
                is Item.PhotoItem -> R.layout.photolist_photo_view_holder
                else -> throw IllegalArgumentException(
                    "unexpected item. item=${getItem(position)}"
                )
            }
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
            R.layout.photolist_filter_view_holder ->
                HeaderViewHolder(
                    PhotolistFilterViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onOrderChangeRequested,
                    onGridChangeRequested
                )
            R.layout.photolist_photo_view_holder ->
                PhotoViewHolder(
                    PhotolistPhotoViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onPhotoClicked
                )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PagingNetworkStateViewHolder) {
            holder.bindTo(networkState)
        } else {
            val item = getItem(position)
            when {
                holder is HeaderViewHolder && item is Item.Header -> holder.bind(item)
                holder is PhotoViewHolder && item is Item.PhotoItem -> holder.bind(item.entity, isGridMode)
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

    class HeaderViewHolder(
        private val binding: PhotolistFilterViewHolderBinding,
        private val onOrderChangeRequested: (View) -> Unit,
        private val onGridChangeRequested: (View) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(header: Item.Header) {
            binding.containerOrder.setOnClickListener {
                onOrderChangeRequested(it)
            }
            binding.gridFilterIcon.setOnClickListener {
                onGridChangeRequested(it)
            }
            binding.header = header
            binding.executePendingBindings()
        }
    }

    class PhotoViewHolder(
        private val binding: PhotolistPhotoViewHolderBinding,
        private val onPhotoClicked: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo, isGridMode: Boolean) {
            if (isGridMode) {
                val viewWidth = getScreenWidth(binding.root.context) / 2
                binding.photoImage.layoutParams.height = viewWidth
                binding.photoImage.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                val viewWidth = getScreenWidth(binding.root.context)
                binding.photoImage.fitViewSizeToPhoto(viewWidth, photo)
                binding.photoImage.scaleType = ImageView.ScaleType.CENTER
            }
            binding.root.setOnClickListener { onPhotoClicked(photo) }

            binding.photo = photo
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = when {
                oldItem is Item.Header && newItem is Item.Header ->
                    oldItem.currentOrder == newItem.currentOrder
                oldItem is Item.PhotoItem && newItem is Item.PhotoItem ->
                    oldItem.entity == newItem.entity
                else -> false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
