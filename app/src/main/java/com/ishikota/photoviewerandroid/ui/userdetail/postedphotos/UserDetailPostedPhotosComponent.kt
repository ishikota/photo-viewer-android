package com.ishikota.photoviewerandroid.ui.userdetail.postedphotos

import dagger.Subcomponent

@Subcomponent(modules = [UserDetailPostedPhotosModule::class])
interface UserDetailPostedPhotosComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): UserDetailPostedPhotosComponent
    }
    fun inject(fragment: UserDetailPostedPhotosFragment)
}
