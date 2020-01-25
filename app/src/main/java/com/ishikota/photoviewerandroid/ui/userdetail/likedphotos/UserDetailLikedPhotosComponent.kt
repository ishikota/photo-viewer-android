package com.ishikota.photoviewerandroid.ui.userdetail.likedphotos

import dagger.Subcomponent

@Subcomponent(modules = [UserDetailLikedPhotosViewModelModule::class, UserDetailLikedPhotosModule::class])
interface UserDetailLikedPhotosComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): UserDetailLikedPhotosComponent
    }
    fun inject(fragment: UserDetailLikedPhotosFragment)
}
