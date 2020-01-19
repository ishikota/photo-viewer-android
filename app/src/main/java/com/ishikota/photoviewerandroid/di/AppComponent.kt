package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.MainActivity
import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailComponent
import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListComponent
import com.ishikota.photoviewerandroid.ui.photodetail.PhotoDetailComponent
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    NetworkModule::class,
    AppModule::class,
    FragmentComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            networkModule: NetworkModule
        ): AppComponent
    }

    fun inject(activity: MainActivity)
    fun photoListComponent(): PhotoListComponent.Factory
    fun collectionListComponent(): CollectionListComponent.Factory
    fun photoDetailComponent(): PhotoDetailComponent.Factory
    fun collectionDetailComponent(): CollectionDetailComponent.Factory
}
