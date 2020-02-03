package com.ishikota.photoviewerandroid.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.databinding.SearchSuggestionViewHolderBinding

class SearchSuggestionRecyclerViewAdapter(
    private val onSuggestionSelected: (String) -> Unit,
    private val onSuggestionLiftRequested: (String) -> Unit
) : RecyclerView.Adapter<SearchSuggestionRecyclerViewAdapter.VH>() {

    data class Item(val word: String)

    private var items = emptyList<Item>()

    fun update(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            SearchSuggestionViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onSuggestionSelected,
            onSuggestionLiftRequested
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.count()

    class VH(
        private val binding: SearchSuggestionViewHolderBinding,
        private val onSuggestionSelected: (String) -> Unit,
        private val onSuggestionLiftRequested: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.root.setOnClickListener {
                onSuggestionSelected(item.word)
            }
            binding.iconLiftSuggestion.setOnClickListener {
                onSuggestionLiftRequested(item.word)
            }

            binding.item = item
            binding.executePendingBindings()
        }
    }
}
