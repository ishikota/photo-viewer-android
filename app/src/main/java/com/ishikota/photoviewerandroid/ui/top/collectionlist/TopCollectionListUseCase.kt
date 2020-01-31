package com.ishikota.photoviewerandroid.ui.top.collectionlist

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListUseCase
import io.reactivex.Single

class TopCollectionListUseCase(
    private val collectionRepository: CollectionRepository
) :CollectionListUseCase<Unit> {
    override fun execute(page: Int, params: Unit): Single<List<Collection>> =
        collectionRepository.getCollections(page)
}
