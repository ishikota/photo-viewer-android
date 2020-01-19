package com.ishikota.photoviewerandroid.ui.collectiondeatil

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class CollectionDetailModule {
    @Provides
    fun provideUseCase(
        collectionRepository: CollectionRepository
    ): CollectionDetailUseCase = CollectionDetailUseCaseImpl(collectionRepository)
}

@Module
abstract class CollectionDetailViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CollectionDetailViewModel::class)
    abstract fun bindPhotoDetailViewModel(viewModel: CollectionDetailViewModel): ViewModel
}
