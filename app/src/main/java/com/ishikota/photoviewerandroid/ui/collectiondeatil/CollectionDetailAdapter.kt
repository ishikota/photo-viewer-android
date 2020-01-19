package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.databinding.CollectiondetailCollectionViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotolistPhotoViewHolderBinding
import com.ishikota.photoviewerandroid.infra.fitViewSizeToPhoto

class CollectionDetailAdapter(
    private val onUserClicked: (User) -> Unit,
    private val onPhotoClicked: (Photo) -> Unit
) : ListAdapter<CollectionDetailAdapter.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Item.CollectionItem -> R.layout.collectiondetail_collection_view_holder
        is Item.PhotoItem -> R.layout.photolist_photo_view_holder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.collectiondetail_collection_view_holder -> CollectionViewHolder(
                CollectiondetailCollectionViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onUserClicked,
                onPhotoClicked
            )
            R.layout.photolist_photo_view_holder -> PhotoViewHolder(
                PhotolistPhotoViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onPhotoClicked
            )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is CollectionViewHolder && item is Item.CollectionItem -> holder.bind(item)
            holder is PhotoViewHolder && item is Item.PhotoItem -> holder.bind(item)
        }
    }

    sealed class Item {
        data class CollectionItem(val entity: Collection, val hasDescription: Boolean) : Item()
        data class PhotoItem(val entity: Photo) : Item()
    }

    private class CollectionViewHolder(
        private val binding: CollectiondetailCollectionViewHolderBinding,
        private val onUserClicked: (User) -> Unit,
        private val onPhotoClicked: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item.CollectionItem) {
            binding.userThumbnail.setOnClickListener {
                onUserClicked(item.entity.user)
            }
            binding.coverImage.setOnClickListener {
                onPhotoClicked(item.entity.coverPhoto)
            }
            binding.collection = item.entity
            binding.executePendingBindings()

            if (!item.hasDescription) {
                binding.description.setText(R.string.no_description)
                binding.description.typeface = Typeface.DEFAULT_BOLD
            }
        }
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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = when {
                oldItem is Item.PhotoItem && newItem is Item.PhotoItem ->
                    oldItem.entity.id == newItem.entity.id
                oldItem is Item.CollectionItem && newItem is Item.CollectionItem ->
                    oldItem.entity.id == newItem.entity.id
                else -> false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
