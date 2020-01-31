package com.ishikota.photoviewerandroid.ui.search.photolist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.databinding.PhotolistFragmentBinding
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListViewModel
import com.ishikota.photoviewerandroid.ui.search.SearchFragmentDirections
import com.ishikota.photoviewerandroid.ui.search.SearchViewModel
import javax.inject.Inject

class SearchPhotoListFragment : Fragment() {

    private lateinit var binding: PhotolistFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    @Inject
    lateinit var pagingRepository: PhotoListPagingRepository<String>

    private val viewModel: PhotoListViewModel<String> by lazy {
        ViewModelProviders.of(
            this, PhotoListViewModel.Factory(pagingRepository)
        ).get(PhotoListViewModel::class.java) as PhotoListViewModel<String>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().searchPhotoListComponent().create().inject(this)
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
            onOrderChangeRequested = { /* Never called in this fragment */ },
            onGridChangeRequested = { /* Never called in this fragment */ }
        )
        binding.recyclerView.adapter = adapter
        adapter.updateLayoutManager(binding.recyclerView, isGridMode = true)

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

        // Observe parent fragment's search query and update search result
        val searchViewModel =
            ViewModelProviders.of(requireParentFragment()).get(SearchViewModel::class.java)
        searchViewModel.searchQuery.observe(this, NonNullObserver {
            viewModel.updateLoadParams(it)
        })

    }

    private fun navigateToPhotoDetail(photo: Photo) {
        val action = SearchFragmentDirections.actionSearchFragmentToPhotoDetailFragment(photo.id)
        findNavController().navigate(action)
    }
}
