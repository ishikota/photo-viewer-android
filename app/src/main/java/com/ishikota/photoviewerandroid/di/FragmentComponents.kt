package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListComponent
import com.ishikota.photoviewerandroid.ui.photodetail.PhotoDetailComponent
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListComponent
import dagger.Module

@Module(subcomponents = [
    PhotoListComponent::class,
    CollectionListComponent::class,
    PhotoDetailComponent::class
])
class FragmentComponents
