package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.databinding.CollectiondetailCarouselCollectionViewHolderBinding

class CollectionDetailCarouselAdapter(
    private val item: CollectionDetailAdapter.Item.RelatedCollections,
    private val onRelatedCollectionClicked: (Collection) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = item.collections.size

    override fun getItemViewType(position: Int): Int =
        R.layout.collectiondetail_carousel_collection_view_holder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.collectiondetail_carousel_collection_view_holder ->
                CollectionViewHolder(
                    CollectiondetailCarouselCollectionViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onRelatedCollectionClicked
                )
            else -> throw IllegalArgumentException("unexpected viewType. viewType = $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val  item = item.collections[position]
        when (holder) {
            is CollectionViewHolder -> holder.bind(item)
        }
    }

    private class CollectionViewHolder(
        private val binding: CollectiondetailCarouselCollectionViewHolderBinding,
        private val onRelatedCollectionClicked: (Collection) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Collection) {
            binding.coverImage.setOnClickListener {
                onRelatedCollectionClicked(item)
            }

            binding.collection = item
            binding.executePendingBindings()
        }
    }
}
