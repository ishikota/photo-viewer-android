package com.ishikota.photoviewerandroid.ui.top

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TopViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TopViewModel::class)
    abstract fun bindViewModel(viewModel: TopViewModel): ViewModel
}
