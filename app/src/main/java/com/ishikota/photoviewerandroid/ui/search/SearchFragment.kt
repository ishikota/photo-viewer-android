package com.ishikota.photoviewerandroid.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.SearchFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.dismissKeyboard
import com.ishikota.photoviewerandroid.ui.search.photolist.SearchPhotoListFragment
import javax.inject.Inject

class SearchFragment : Fragment() {

    private val safeArgs: SearchFragmentArgs by navArgs()

    private lateinit var binding: SearchFragmentBinding

    private lateinit var adapter: SearchSuggestionRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(SearchViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().searchComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(NavHostFragment.findNavController(this))

        adapter = SearchSuggestionRecyclerViewAdapter(
            onSuggestionSelected = { suggestion ->
                binding.editQuery.setText(suggestion)
                startSearch()
                dismissKeyboard(activity, binding.editQuery)
            },
            onSuggestionLiftRequested = { suggestion ->
                binding.editQuery.setText(suggestion)
                dismissKeyboard(activity, binding.editQuery)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { viewModel.requestSuggestion(it.toString()) }
            }
        })
        binding.editQuery.setOnEditorActionListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                startSearch()
                dismissKeyboard(activity, binding.editQuery)
                true
            } else {
                false
            }
        }
        binding.buttonSearch.setOnClickListener {
            startSearch()
            dismissKeyboard(activity, binding.editQuery)
        }

        viewModel.suggestion.observe(this, NonNullObserver {
            binding.recyclerView.visibility = View.VISIBLE
            adapter.update(it)
        })

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SearchPhotoListFragment())
            .commit()

        safeArgs.searchWord?.let {
            binding.editQuery.setText(it)
            startSearch()
        }
    }

    private fun startSearch() {
        val query = binding.editQuery.text.toString()
        viewModel.startSearch(query)
    }
}
