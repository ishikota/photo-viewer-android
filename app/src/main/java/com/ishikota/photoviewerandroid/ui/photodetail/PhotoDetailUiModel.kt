package com.ishikota.photoviewerandroid.ui.photodetail

import com.ishikota.photoviewerandroid.data.api.entities.Photo

data class PhotoDetailUiModel(
    var photo: Photo,
    var isLikeUpdating: Boolean = false
) {

    fun toRecyclerViewData(): List<PhotoDetailAdapter.Item> {
        val items = mutableListOf<PhotoDetailAdapter.Item>(
            PhotoDetailAdapter.Item.PhotoItem(photo, isLikeUpdating)
        )
        photo.description?.let {
            items.add(PhotoDetailAdapter.Item.DescriptionItem(it, isAlt = false))
        }
        photo.altDescription?.let {
            items.add(PhotoDetailAdapter.Item.DescriptionItem(it, isAlt = true))
        }
        if (photo.tags?.isNotEmpty() == true) {
            items.add(PhotoDetailAdapter.Item.TagsItem(photo.tags!!))
        }
        photo.currentUserCollections.forEach { collection ->
            items.add(PhotoDetailAdapter.Item.CollectionItem(collection))
        }
        return items
    }
}
