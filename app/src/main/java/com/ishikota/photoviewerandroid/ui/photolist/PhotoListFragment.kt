package com.ishikota.photoviewerandroid.ui.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.databinding.PhotolistFragmentBinding
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status

class PhotoListFragment : Fragment() {

    private lateinit var binding: PhotolistFragmentBinding

    private val viewModel: PhotoListViewModel by lazy {
        ViewModelProviders.of(
            this, PhotoListViewModel.Factory(
                PhotoListPagingRepository(
                    LoadPhotoListUseCaseImpl(PhotoRepository.Factory.create())
                )
            )
        ).get(PhotoListViewModel::class.java)
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

        val adapter = PhotoListAdapter(
            retryCallback = { viewModel.retry() },
            onPhotoClicked = { _ ->
                Toast.makeText(
                    requireContext(),
                    "photo clicked",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onOrderChangeRequested = {
                Toast.makeText(
                    requireContext(),
                    "onOrderChangeRequested",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onGridChangeRequested = {
                Toast.makeText(
                    requireContext(),
                    "onGridChangeRequested",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

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
}
