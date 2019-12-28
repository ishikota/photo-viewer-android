package com.ishikota.photoviewerandroid.infra

import android.view.View
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import kotlin.math.roundToInt

fun View.fitViewSizeToPhoto(expectedViewWidth: Int, photo: Photo) {
    val params = layoutParams
    params.height = photo.calculateViewHeight(expectedViewWidth)
    layoutParams = params
}

fun Photo.calculateViewHeight(viewWidth: Int): Int = (viewWidth * (1.0 * height / width)).roundToInt()
