package com.ishikota.photoviewerandroid.ui.userdetail.likedphotos

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.di.ViewModelKey
import com.ishikota.photoviewerandroid.ui.userdetail.photolist.UserDetailPhotoListPagingRepository
import com.ishikota.photoviewerandroid.ui.userdetail.photolist.UserDetailPhotoListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class UserDetailLikedPhotosModule {
    @Provides
    fun provideUserDetailPostedPhotosPagingRepository(
        userRepository: UserRepository
    ): UserDetailPhotoListPagingRepository =
        UserDetailPhotoListPagingRepository(
            UserDetailLikedPhotosUseCase(userRepository)
        )
}

@Module
abstract class UserDetailLikedPhotosViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserDetailPhotoListViewModel::class)
    abstract fun bindViewModel(viewModel: UserDetailPhotoListViewModel): ViewModel
}
