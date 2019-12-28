package com.ishikota.photoviewerandroid.ui.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.databinding.PhotolistFragmentBinding
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import timber.log.Timber

class PhotoListFragment : Fragment() {

    private lateinit var binding: PhotolistFragmentBinding

    private val viewModel: PhotoListViewModel by lazy {
        ViewModelProviders.of(
            this, PhotoListViewModel.Factory(
                PhotoListPagingRepository(
                    PhotoRepository.Factory.create()
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

        val adapter = PhotoListAdapter(
            retryCallback = { viewModel.retry() },
            onPhotoClicked = { _ ->
                Toast.makeText(
                    requireContext(),
                    "photo clicked",
                    Toast.LENGTH_SHORT
                ).show()
            })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.pagedList.observe(this, NonNullObserver {
            adapter.submitList(it)
        })
        viewModel.initialLoadNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
            // TODO Workaround because RecyclerView is scrolled to last item of first page after initial load
            binding.recyclerView.scrollToPosition(0)
        })
        viewModel.loadMoreNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
        })

    }
}
