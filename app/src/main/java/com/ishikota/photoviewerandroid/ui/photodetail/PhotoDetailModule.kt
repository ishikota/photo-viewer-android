package com.ishikota.photoviewerandroid.ui.photodetail

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PhotoDetailViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PhotoDetailViewModel::class)
    abstract fun bindPhotoDetailViewModel(viewModel: PhotoDetailViewModel): ViewModel
}
