package com.ishikota.photoviewerandroid.di

import androidx.fragment.app.Fragment
import com.ishikota.photoviewerandroid.PhotoViewerApplication

fun Fragment.appComponent() = (requireActivity().application as PhotoViewerApplication).appComponent
