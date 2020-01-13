package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class PhotoListModule {
    @Provides
    fun providePhotoListPagingRepository(
        photoRepository: PhotoRepository
    ): PhotoListPagingRepository =
        PhotoListPagingRepository(
            LoadPhotoListUseCaseImpl(photoRepository)
        )
}

@Module
abstract class PhotoListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PhotoListViewModel::class)
    abstract fun bindPhotoListViewModel(viewModel: PhotoListViewModel): ViewModel
}
