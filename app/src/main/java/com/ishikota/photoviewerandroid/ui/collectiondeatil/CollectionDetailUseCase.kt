package com.ishikota.photoviewerandroid.ui.collectiondeatil

import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

interface CollectionDetailUseCase {
    fun execute(collectionId: String, page: Int): Single<List<CollectionDetailAdapter.Item>>
}

class CollectionDetailUseCaseImpl @Inject constructor(
    private val collectionRepository: CollectionRepository
) : CollectionDetailUseCase {

    override fun execute(
        collectionId: String,
        page: Int
    ): Single<List<CollectionDetailAdapter.Item>> =
        Single.zip(
            collectionRepository.getCollectionPhotos(collectionId, page),
            collectionRepository.getCollectionsRelatedCollections(collectionId),
            BiFunction { photos, relatedCollections ->
                val items: MutableList<CollectionDetailAdapter.Item> =
                    photos.map { CollectionDetailAdapter.Item.PhotoItem(it) }.toMutableList()

                if (page == 1
                    && relatedCollections.size >= MINIMUM_RELATED_COLLECTION_COUNT
                    && items.size >= MINIMUM_RELATED_COLLECTION_COUNT
                ) {
                    items.add(
                        CAROUSEL_INSERT_POSITION,
                        CollectionDetailAdapter.Item.RelatedCollections(relatedCollections)
                    )
                } else {
                    Timber.d("Do not insert carousel")
                }
                items
            }
        )

    companion object {
        // Show carousel if the collection has more than 3 related collections
        private const val MINIMUM_RELATED_COLLECTION_COUNT = 3

        private const val CAROUSEL_INSERT_POSITION = 3
    }
}
