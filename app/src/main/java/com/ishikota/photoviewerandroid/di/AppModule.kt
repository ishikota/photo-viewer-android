package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.repository.*
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

    @Singleton
    @Provides
    fun provideUserRepository(
        service: PhotoViewerService
    ): UserRepository {
        return UserRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        service: PhotoViewerService
    ): SearchRepository {
        return SearchRepositoryImpl(service)
    }
}
