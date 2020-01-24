package com.ishikota.photoviewerandroid.ui.collectiondeatil

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface CollectionDetailUseCase {
    fun execute(collectionId: String, page: Int): Single<List<CollectionDetailAdapter.Item>>
}

class CollectionDetailUseCaseImpl @Inject constructor(
    private val collectionRepository: CollectionRepository
) : CollectionDetailUseCase {
    override fun execute(collectionId: String, page: Int): Single<List<CollectionDetailAdapter.Item>> {
        return if (page == 1) {
            collectionRepository.getCollection(collectionId).zipWith(
                collectionRepository.getCollectionPhotos(collectionId, page), BiFunction(this::merge)
            )
        } else {
            collectionRepository.getCollectionPhotos(collectionId, page).map { photos ->
                photos.map { CollectionDetailAdapter.Item.PhotoItem(it) }
            }
        }
    }


    private fun merge(
        collection: Collection, photos: List<Photo>
    ): List<CollectionDetailAdapter.Item> {
        val items = mutableListOf<CollectionDetailAdapter.Item>()
        items.add(CollectionDetailAdapter.Item.CollectionItem(
            collection, collection.description != null
        ))
        for (photo in photos) {
            items.add(CollectionDetailAdapter.Item.PhotoItem(photo))
        }
        return items
    }

}
