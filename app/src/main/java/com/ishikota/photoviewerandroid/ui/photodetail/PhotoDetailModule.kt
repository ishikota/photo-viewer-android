package com.ishikota.photoviewerandroid.ui.photodetail

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class PhotoDetailModule {
    @Provides
    fun provideUseCase(
        photoRepository: PhotoRepository
    ): PhotoDetailUseCase = PhotoDetailUseCaseImpl(photoRepository)
}

@Module
abstract class PhotoDetailViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PhotoDetailViewModel::class)
    abstract fun bindPhotoDetailViewModel(viewModel: PhotoDetailViewModel): ViewModel
}
