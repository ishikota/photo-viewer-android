package com.ishikota.photoviewerandroid.ui.photodetail

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.databinding.PhotodetailDescriptionViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotodetailPhotoViewHolderBinding
import com.ishikota.photoviewerandroid.databinding.PhotodetailTagsViewHolderBinding

class PhotoDetailAdapter(
    private val onUserClicked: (User) -> Unit,
    private val onTagClicked: (Photo.Tag) -> Unit,
    private val onShareClicked: (Photo) -> Unit
) : ListAdapter<PhotoDetailAdapter.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Item.PhotoItem -> R.layout.photodetail_photo_view_holder
        is Item.DescriptionItem -> R.layout.photodetail_description_view_holder
        is Item.TagsItem -> R.layout.photodetail_tags_view_holder
        is Item.CollectionItem -> R.layout.photodetail_photo_view_holder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.photodetail_photo_view_holder -> PhotoViewHolder(
                PhotodetailPhotoViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onUserClicked,
                onShareClicked
            )
            R.layout.photodetail_description_view_holder -> DescriptionViewHolder(
                PhotodetailDescriptionViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            R.layout.photodetail_tags_view_holder -> TagsViewHolder(
                PhotodetailTagsViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onTagClicked
            )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is PhotoViewHolder && item is Item.PhotoItem -> holder.bind(item)
            holder is DescriptionViewHolder && item is Item.DescriptionItem -> holder.bind(item)
            holder is TagsViewHolder && item is Item.TagsItem -> holder.bind(item)
        }
    }

    sealed class Item {
        data class PhotoItem(val entity: Photo) : Item()
        data class DescriptionItem(val description: String, val isAlt: Boolean) : Item()
        data class TagsItem(val tags: List<Photo.Tag>) : Item()
        data class CollectionItem(val entity: Collection) : Item()
    }

    private class PhotoViewHolder(
        private val binding: PhotodetailPhotoViewHolderBinding,
        private val onUserClicked: (User) -> Unit,
        private val onShareClicked: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item.PhotoItem) {
            binding.userContainer.setOnClickListener {
                onUserClicked(item.entity.user)
            }
            binding.iconShare.setOnClickListener {
                onShareClicked(item.entity)
            }
            binding.photo = item
            binding.executePendingBindings()
        }
    }

    private class DescriptionViewHolder(
        private val binding: PhotodetailDescriptionViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item.DescriptionItem) {
            binding.item = item
            binding.executePendingBindings()
            binding.description.typeface =
                if (item.isAlt) Typeface.DEFAULT else Typeface.DEFAULT_BOLD
        }
    }

    private class TagsViewHolder(
        private val binding: PhotodetailTagsViewHolderBinding,
        private val onTagClicked: (Photo.Tag) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val inflater = LayoutInflater.from(binding.root.context)

        fun bind(item: Item.TagsItem) {
            binding.tagContainer.removeAllViewsInLayout()
            for (tag in item.tags) {
                val chip = inflater.inflate(R.layout.tag, binding.tagContainer, false) as Chip
                chip.text = tag.title
                chip.setOnClickListener {
                    onTagClicked(tag)
                }
                binding.tagContainer.addView(chip)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = when {
                oldItem is Item.PhotoItem && newItem is Item.PhotoItem ->
                    oldItem.entity.id == newItem.entity.id
                oldItem is Item.CollectionItem && newItem is Item.CollectionItem ->
                    oldItem.entity == newItem.entity
                else -> false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
