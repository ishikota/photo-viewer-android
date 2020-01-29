package com.ishikota.photoviewerandroid.ui.top.photolist

import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import dagger.Module
import dagger.Provides

@Module
class TopPhotoListModule {
    @Provides
    fun providePhotoListPagingRepository(
        photoRepository: PhotoRepository
    ): PhotoListPagingRepository<PhotoRepository.Order> =
        PhotoListPagingRepository(
            TopPhotoListUseCase(photoRepository)
        )
}
