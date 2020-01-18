package com.ishikota.photoviewerandroid.ui.photodetail

import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import io.reactivex.Single
import javax.inject.Inject

interface PhotoDetailUseCase {
    fun execute(id: String): Single<List<PhotoDetailAdapter.Item>>
}

class PhotoDetailUseCaseImpl @Inject constructor(
    private val photoRepository: PhotoRepository
): PhotoDetailUseCase {
    override fun execute(id: String): Single<List<PhotoDetailAdapter.Item>> =
        photoRepository.getPhoto(id).map { photo ->
            val items = mutableListOf<PhotoDetailAdapter.Item>(
                PhotoDetailAdapter.Item.PhotoItem(photo)
            )
            photo.description?.let {
                items.add(PhotoDetailAdapter.Item.DescriptionItem(it, isAlt = false))
            }
            photo.altDescription?.let {
                items.add(PhotoDetailAdapter.Item.DescriptionItem(it, isAlt = true))
            }
            if (photo.tags != null && photo.tags.isNotEmpty()) {
                items.add(PhotoDetailAdapter.Item.TagsItem(photo.tags))
            }
            photo.currentUserCollections.forEach { collection ->
                items.add(PhotoDetailAdapter.Item.CollectionItem(collection))
            }
            items
        }
}
