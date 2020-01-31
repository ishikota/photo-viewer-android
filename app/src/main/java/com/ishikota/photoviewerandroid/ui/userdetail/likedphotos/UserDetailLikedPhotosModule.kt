package com.ishikota.photoviewerandroid.ui.userdetail.likedphotos

import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import dagger.Module
import dagger.Provides

@Module
class UserDetailLikedPhotosModule {
    @Provides
    fun providePhotoListPagingRepository(
        userRepository: UserRepository
    ): PhotoListPagingRepository<String> =
        PhotoListPagingRepository(
            UserDetailLikedPhotosUseCase(userRepository)
        )
}
