package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ishikota.photoviewerandroid.R

@BindingAdapter("collectionDetailDescription")
fun TextView.setCollectionDetailDescription(description: String?) {
    if (description == null || description.isEmpty()) {
        setText(R.string.no_description)
        typeface = Typeface.DEFAULT_BOLD
    } else {
        text = description
        typeface = Typeface.DEFAULT
    }
}
