package com.ishikota.photoviewerandroid.ui.userdetail.postedphotos

import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import dagger.Module
import dagger.Provides

@Module
class UserDetailPostedPhotosModule {
    @Provides
    fun provideUserDetailPostedPhotosPagingRepository(
        userRepository: UserRepository
    ): PhotoListPagingRepository<String> =
        PhotoListPagingRepository(
            UserDetailPostedPhotosUseCase(userRepository)
        )
}
