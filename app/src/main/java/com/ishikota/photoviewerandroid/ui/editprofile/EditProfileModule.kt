package com.ishikota.photoviewerandroid.ui.editprofile

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EditProfileViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindViewModel(viewModel: EditProfileViewModel): ViewModel
}

