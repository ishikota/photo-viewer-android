package com.ishikota.photoviewerandroid.ui.collectiondeatil

import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import io.reactivex.Single
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
        collectionRepository.getCollectionPhotos(collectionId, page).map { photos ->
            photos.map { CollectionDetailAdapter.Item.PhotoItem(it) }
        }
}
