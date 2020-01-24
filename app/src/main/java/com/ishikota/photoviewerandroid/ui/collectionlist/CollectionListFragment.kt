package com.ishikota.photoviewerandroid.ui.collectionlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.databinding.CollectionlistFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status
import com.ishikota.photoviewerandroid.ui.top.TopFragmentDirections
import javax.inject.Inject

class CollectionListFragment : Fragment(), TabElement {

    override val title: Int? = R.string.top_tab_collection

    override val iconResId: Int? = null

    private lateinit var binding: CollectionlistFragmentBinding

    private lateinit var adapter: CollectionListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CollectionListViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(CollectionListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().collectionListComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CollectionlistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        adapter = CollectionListAdapter(
            retryCallback = { viewModel.retry() },
            onCollectionClicked = this::navigateToCollectionDetail,
            onUserClicked = { _ ->
                Toast.makeText(
                    requireContext(),
                    "user clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.pagedList.observe(this, NonNullObserver {
            adapter.submitList(it)
        })
        viewModel.initialLoadNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
//            // TODO Workaround because RecyclerView is scrolled to last item of first page after initial load
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

    private fun navigateToCollectionDetail(collection: Collection) {
        val action = TopFragmentDirections.actionTopFragmentToCollectionDetailFragment(
            collection.id,
            collection.title
        )
        findNavController().navigate(action)
    }
}
