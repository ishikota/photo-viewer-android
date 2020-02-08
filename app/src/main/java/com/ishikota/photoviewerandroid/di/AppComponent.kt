package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.MainActivity
import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailComponent
import com.ishikota.photoviewerandroid.ui.photodetail.PhotoDetailComponent
import com.ishikota.photoviewerandroid.ui.search.SearchComponent
import com.ishikota.photoviewerandroid.ui.search.photolist.SearchPhotoListComponent
import com.ishikota.photoviewerandroid.ui.top.collectionlist.TopCollectionListComponent
import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListComponent
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailComponent
import com.ishikota.photoviewerandroid.ui.userdetail.likedphotos.UserDetailLikedPhotosComponent
import com.ishikota.photoviewerandroid.ui.userdetail.postedphotos.UserDetailPostedPhotosComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    NetworkModule::class,
    PreferenceModule::class,
    AppModule::class,
    FragmentComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            networkModule: NetworkModule,
            preferenceModule: PreferenceModule
        ): AppComponent
    }

    fun inject(activity: MainActivity)
    fun topPhotoListComponent(): TopPhotoListComponent.Factory
    fun topCollectionListComponent(): TopCollectionListComponent.Factory
    fun photoDetailComponent(): PhotoDetailComponent.Factory
    fun collectionDetailComponent(): CollectionDetailComponent.Factory
    fun userDetailComponent(): UserDetailComponent.Factory
    fun userDetailPostedPhotosComponent(): UserDetailPostedPhotosComponent.Factory
    fun userDetailLikedPhotosComponent(): UserDetailLikedPhotosComponent.Factory
    fun searchComponent(): SearchComponent.Factory
    fun searchPhotoListComponent(): SearchPhotoListComponent.Factory
}
