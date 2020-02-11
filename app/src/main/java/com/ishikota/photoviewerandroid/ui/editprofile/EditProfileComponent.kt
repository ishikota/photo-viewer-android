package com.ishikota.photoviewerandroid.ui.editprofile

import dagger.Subcomponent

@Subcomponent(modules = [EditProfileViewModelModule::class])
interface EditProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): EditProfileComponent
    }
    fun inject(fragment: EditProfileFragment)
}
