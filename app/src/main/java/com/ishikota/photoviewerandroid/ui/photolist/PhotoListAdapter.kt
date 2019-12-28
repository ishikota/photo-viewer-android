package com.ishikota.photoviewerandroid.ui.photolist

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.databinding.PagingNetworkStateViewHolderBinding
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import android.view.LayoutInflater
import androidx.core.hardware.display.DisplayManagerCompat
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.PhotolistPhotoViewHolderBinding
import com.ishikota.photoviewerandroid.infra.fitViewSizeToPhoto
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkStateViewHolder


class PhotoListAdapter(
    private val retryCallback: () -> Unit,
    private val onPhotoClicked: (Photo) -> Unit
) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var networkState: PagingNetworkState? = null

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.paging_network_state_view_holder
        } else {
            R.layout.photolist_photo_view_holder
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
        when (holder) {
            is PagingNetworkStateViewHolder -> holder.bindTo(networkState)
            is PhotoViewHolder -> getItem(position)?.let { holder.bind(it) }
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

    class PhotoViewHolder(
        private val binding: PhotolistPhotoViewHolderBinding,
        private val onPhotoClicked: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            binding.photoImage.fitViewSizeToPhoto(getScreenWidth(binding.root.context), photo)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }
}
