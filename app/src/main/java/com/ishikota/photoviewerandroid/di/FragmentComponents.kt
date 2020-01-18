package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListComponent
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListComponent
import dagger.Module

@Module(subcomponents = [
    PhotoListComponent::class,
    CollectionListComponent::class
])
class FragmentComponents
