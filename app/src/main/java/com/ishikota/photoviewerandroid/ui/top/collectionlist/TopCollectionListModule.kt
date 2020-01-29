package com.ishikota.photoviewerandroid.ui.top.collectionlist

import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListPagingRepository
import dagger.Module
import dagger.Provides

@Module
class TopCollectionListModule {
    @Provides
    fun provideCollectionListPagingRepository(
        collectionRepository: CollectionRepository
    ): CollectionListPagingRepository<Unit> =
        CollectionListPagingRepository(
            TopCollectionListUseCase(collectionRepository)
        )
}
