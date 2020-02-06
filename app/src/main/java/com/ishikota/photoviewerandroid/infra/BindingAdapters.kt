package com.ishikota.photoviewerandroid.infra

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ishikota.photoviewerandroid.infra.glide.GlideApp
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        GlideApp.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("visibleWhen")
fun visibleWhen(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("hideProgress")
fun SwipeRefreshLayout.hideProgress(networkState: PagingNetworkState?) {
    if (networkState != null && networkState.status != Status.RUNNING) {
        isRefreshing = false
    }
}

@BindingAdapter("clipToCircle")
fun clipToCircle(view: View, clip: Boolean) {
    view.clipToOutline = clip
    view.outlineProvider = if (clip) CircularOutlineProvider else null
}

private object CircularOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(
            view.paddingLeft,
            view.paddingTop,
            view.width - view.paddingRight,
            view.height - view.paddingBottom
        )
    }
}

@BindingAdapter("roundCorner")
fun roundCorner(view: View, radius: Float) {
    val roundOutlineProvider = object: ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
    view.clipToOutline = true
    view.outlineProvider = roundOutlineProvider
}
