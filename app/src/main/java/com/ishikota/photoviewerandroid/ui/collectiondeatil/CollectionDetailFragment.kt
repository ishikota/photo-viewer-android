package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.databinding.CollectiondetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import javax.inject.Inject

class CollectionDetailFragment : Fragment() {

    private val safeArgs: CollectionDetailFragmentArgs by navArgs()

    private lateinit var binding: CollectiondetailFragmentBinding

    private lateinit var adapter: CollectionDetailAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CollectionDetailViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(CollectionDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().collectionDetailComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CollectiondetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(NavHostFragment.findNavController(this))
        binding.toolbar.title = safeArgs.collectionTitle

        adapter = CollectionDetailAdapter(
            onUserClicked = { user ->
                Toast.makeText(
                    requireContext(),
                    "user clicked",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onPhotoClicked = this::navigateToPhotoDetail,
            retryCallback = { viewModel.retry() }
        )
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
        viewModel.setCollectionId(safeArgs.collectionId)
    }

    private fun navigateToPhotoDetail(photo: Photo) {
        val action = CollectionDetailFragmentDirections.
            actionCollectionDetailFragmentToPhotoDetailFragment(photo.id)
        findNavController().navigate(action)
    }

}
