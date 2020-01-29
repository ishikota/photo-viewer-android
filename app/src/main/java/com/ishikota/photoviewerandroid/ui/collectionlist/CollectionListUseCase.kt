package com.ishikota.photoviewerandroid.ui.collectionlist

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import io.reactivex.Single

interface CollectionListUseCase<P> {
    fun execute(page: Int, params: P) : Single<List<Collection>>
}
