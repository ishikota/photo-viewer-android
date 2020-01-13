package com.ishikota.photoviewerandroid.ui.photolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishikota.photoviewerandroid.PhotoViewerApplication
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.databinding.PhotolistFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status
import javax.inject.Inject

class PhotoListFragment : Fragment(), TabElement {

    override val title: Int? = R.string.top_tab_photo

    override val iconResId: Int? = null

    private lateinit var binding: PhotolistFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PhotoListViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(PhotoListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as PhotoViewerApplication)
            .appComponent.photoListComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PhotolistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        adapter = PhotoListAdapter(
            retryCallback = { viewModel.retry() },
            onPhotoClicked = { _ ->
                Toast.makeText(
                    requireContext(),
                    "photo clicked",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onOrderChangeRequested = this::showListOrderPopupMenu,
            onGridChangeRequested = this::showSwitchGridModePopupMenu
        )
        binding.recyclerView.adapter = adapter
        setLayoutManager(adapter.isGridMode)

        viewModel.pagedList.observe(this, NonNullObserver {
            adapter.submitList(it)
        })
        viewModel.initialLoadNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
            // TODO Workaround because RecyclerView is scrolled to last item of first page after initial load
            binding.recyclerView.scrollToPosition(0)
            // Hide adapter's progress while swipe refreshing
            if (binding.swipeRefresh.isRefreshing && it.status == Status.RUNNING) {
                adapter.setNetworkState(PagingNetworkState.LOADED)
            }
        })
        viewModel.loadMoreNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
        })

    }

    private fun setLayoutManager(isGridMode: Boolean) {
        val layoutManager = if (isGridMode) {
            GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        when (adapter.getItemViewType(position)) {
                            R.layout.photolist_filter_view_holder,
                            R.layout.paging_network_state_view_holder -> 2
                            else -> 1
                        }
                }
            }
        } else {
            LinearLayoutManager(requireContext())
        }
        adapter.isGridMode = isGridMode
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun showListOrderPopupMenu(v: View) {
        PopupMenu(v.context, v).apply {
            menuInflater.inflate(R.menu.photolist_order, menu)
            setOnMenuItemClickListener { item ->
                val order = when (item.itemId) {
                    R.id.popular -> PhotoRepository.Order.POPULAR
                    R.id.latest -> PhotoRepository.Order.LATEST
                    R.id.oldest -> PhotoRepository.Order.OLDEST
                    else -> throw IllegalArgumentException("unexpected id=${item.itemId}")
                }
                viewModel.updateListOrder(order)
                true
            }
        }.show()
    }

    private fun showSwitchGridModePopupMenu(v: View) {
        PopupMenu(v.context, v).apply {
            menuInflater.inflate(R.menu.photolist_grid, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.linear -> setLayoutManager(isGridMode = false)
                    R.id.grid -> setLayoutManager(isGridMode = true)
                    else -> throw IllegalArgumentException("unexpected id=${item.itemId}")
                }
                true
            }
        }.show()
    }
}
