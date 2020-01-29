package com.ishikota.photoviewerandroid.ui.top.photolist

import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListUseCase
import io.reactivex.Single

class TopPhotoListUseCase(
    private val photoRepository: PhotoRepository
) : PhotoListUseCase<PhotoRepository.Order> {

    override fun execute(
        page: Int,
        params: PhotoRepository.Order
    ): Single<List<PhotoListAdapter.Item>> {
        val photoItems: Single<List<PhotoListAdapter.Item>> =
            photoRepository.getPhotos(page, params).map { photos ->
                photos.map { PhotoListAdapter.Item.PhotoItem(it) }
            }

        return if (page == 1) {
            // Append header item to top of the list
            photoItems.map {
                val items = mutableListOf<PhotoListAdapter.Item>()
                items.add(PhotoListAdapter.Item.Header(params))
                items.addAll(it)
                items
            }
        } else {
            photoItems
        }
    }
}
