package com.ishikota.photoviewerandroid.ui.top.photolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.databinding.PhotolistFragmentBinding
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListViewModel
import com.ishikota.photoviewerandroid.ui.top.TopFragmentDirections
import javax.inject.Inject

class TopPhotoListFragment : Fragment(), TabElement {

    override val title: Int? = R.string.top_tab_photo

    override val iconResId: Int? = null

    private lateinit var binding: PhotolistFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    @Inject
    lateinit var pagingRepository: PhotoListPagingRepository<PhotoRepository.Order>

    private val viewModel: PhotoListViewModel<PhotoRepository.Order> by lazy {
        ViewModelProviders.of(
            this, PhotoListViewModel.Factory(pagingRepository)
        ).get(PhotoListViewModel::class.java) as PhotoListViewModel<PhotoRepository.Order>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().topPhotoListComponent().create().inject(this)
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
            onPhotoClicked = this::navigateToPhotoDetail,
            onOrderChangeRequested = this::showListOrderPopupMenu,
            onGridChangeRequested = this::showSwitchGridModePopupMenu
        )
        binding.recyclerView.adapter = adapter
        adapter.updateLayoutManager(binding.recyclerView, adapter.isGridMode)

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
        viewModel.updateLoadParams(PhotoRepository.Order.POPULAR)

    }

    private fun navigateToPhotoDetail(photo: Photo) {
        val action = TopFragmentDirections.actionTopFragmentToPhotoDetailFragment(photo.id)
        findNavController().navigate(action)
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
                viewModel.updateLoadParams(order)
                true
            }
        }.show()
    }

    private fun showSwitchGridModePopupMenu(v: View) {
        PopupMenu(v.context, v).apply {
            menuInflater.inflate(R.menu.photolist_grid, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.linear -> adapter.updateLayoutManager(
                        binding.recyclerView,
                        isGridMode = false
                    )
                    R.id.grid -> adapter.updateLayoutManager(
                        binding.recyclerView,
                        isGridMode = true
                    )
                    else -> throw IllegalArgumentException("unexpected id=${item.itemId}")
                }
                true
            }
        }.show()
    }
}
