package com.ishikota.photoviewerandroid.ui.top

import dagger.Subcomponent

@Subcomponent(modules = [TopViewModelModule::class])
interface TopComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TopComponent
    }

    fun inject(fragment: TopFragment)
}
