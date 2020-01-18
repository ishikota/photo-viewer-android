package com.ishikota.photoviewerandroid.ui.collectionlist

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class CollectionListModule {
    @Provides
    fun provideCollectionListPagingRepository(
        collectionRepository: CollectionRepository
    ): CollectionListPagingRepository =
        CollectionListPagingRepository(
            collectionRepository
        )
}

@Module
abstract class CollectionListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CollectionListViewModel::class)
    abstract fun bindCollectionListViewModel(viewModel: CollectionListViewModel): ViewModel
}
