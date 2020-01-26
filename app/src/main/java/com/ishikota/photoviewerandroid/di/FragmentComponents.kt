package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailComponent
import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListComponent
import com.ishikota.photoviewerandroid.ui.photodetail.PhotoDetailComponent
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListComponent
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailComponent
import com.ishikota.photoviewerandroid.ui.userdetail.likedphotos.UserDetailLikedPhotosComponent
import com.ishikota.photoviewerandroid.ui.userdetail.postedphotos.UserDetailPostedPhotosComponent
import dagger.Module

@Module(subcomponents = [
    PhotoListComponent::class,
    CollectionListComponent::class,
    PhotoDetailComponent::class,
    CollectionDetailComponent::class,
    UserDetailComponent::class,
    UserDetailPostedPhotosComponent::class,
    UserDetailLikedPhotosComponent::class
])
class FragmentComponents
