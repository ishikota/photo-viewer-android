package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailComponent
import com.ishikota.photoviewerandroid.ui.photodetail.PhotoDetailComponent
import com.ishikota.photoviewerandroid.ui.search.SearchComponent
import com.ishikota.photoviewerandroid.ui.top.collectionlist.TopCollectionListComponent
import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListComponent
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailComponent
import com.ishikota.photoviewerandroid.ui.userdetail.likedphotos.UserDetailLikedPhotosComponent
import com.ishikota.photoviewerandroid.ui.userdetail.postedphotos.UserDetailPostedPhotosComponent
import dagger.Module

@Module(subcomponents = [
    TopPhotoListComponent::class,
    TopCollectionListComponent::class,
    PhotoDetailComponent::class,
    CollectionDetailComponent::class,
    UserDetailComponent::class,
    UserDetailPostedPhotosComponent::class,
    UserDetailLikedPhotosComponent::class,
    SearchComponent::class
])
class FragmentComponents
