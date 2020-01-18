package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.data.repository.CollectionRepositoryImpl
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.data.repository.PhotoRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesCollectionRepository(
        service: PhotoViewerService
    ): CollectionRepository {
        return CollectionRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun providePhotoRepository(
        service: PhotoViewerService
    ): PhotoRepository {
        return PhotoRepositoryImpl(service)
    }
}
