package com.ishikota.photoviewerandroid.ui.userdetail

import dagger.Subcomponent

@Subcomponent(modules = [UserDetailViewModelModule::class])
interface UserDetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserDetailComponent
    }
    fun inject(fragment: UserDetailFragment)
}
