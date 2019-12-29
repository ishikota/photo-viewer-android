package com.ishikota.photoviewerandroid.ui.photolist

import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import io.reactivex.Single

interface LoadPhotoListUseCase {
    fun execute(page: Int, listOrder: PhotoRepository.Order): Single<List<PhotoListAdapter.Item>>
}

class LoadPhotoListUseCaseImpl(
    private val photoRepository: PhotoRepository
) : LoadPhotoListUseCase {
    override fun execute(
        page: Int,
        listOrder: PhotoRepository.Order
    ): Single<List<PhotoListAdapter.Item>> {
        val photoItems: Single<List<PhotoListAdapter.Item>> =
            photoRepository.getPhotos(page, listOrder).map { photos ->
                photos.map { PhotoListAdapter.Item.PhotoItem(it) }
            }

        return if (page == 1) {
            // Append header item to top of the list
            photoItems.map {
                val items = mutableListOf<PhotoListAdapter.Item>()
                items.add(PhotoListAdapter.Item.Header(listOrder))
                items.addAll(it)
                items
            }
        } else {
            photoItems
        }
    }
}
