package com.alansoft.kacote.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.databinding.FragmentSearchBinding
import com.alansoft.kacote.ui.Base.ResultAdapter
import com.alansoft.kacote.ui.main.PlaceholderFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class SearchFragment :
    PlaceholderFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    private fun doSearch(query: String) {
        binding.query = query
        viewModel.setQuery(query)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("requestKey") { key, bundle ->
            if (key == "requestKey") {
                val query = bundle.getString("query")
                query?.let {
                    doSearch(it)
                }
            }
        }

        viewModel.run {
            results.observe(viewLifecycleOwner, { result ->
                when (result.status) {
                    Resource.Status.SUCCESS -> {
                        binding.emptyResult =
                            result.data?.imageMeta?.pageable_count == 0 && result.data.vClipMeta.pageable_count == 0
                        result.data?.documents?.let {
                            adapter.submitList(it)
                        } ?: adapter.submitList(null)
                    }
                    Resource.Status.ERROR -> {
                        binding.emptyResult = true
                    }
                    else -> {

                    }
                }
            })

            loadMoreStatus.observe(viewLifecycleOwner, { loadingMore ->
                if (loadingMore == null) {
                    binding.loadingMore = false
                } else {
                    binding.loadingMore = loadingMore.isRunning
                    val error = loadingMore.errorMessageIfNotHandled
                    if (error != null) {
//                        Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }

        binding.serachRv.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
//                    if (lastPosition == adapter?.itemCount ?: 0 - 1) {
//                        viewModel.loadNextPage()
//                    }
                }
            })
            adapter = this@SearchFragment.adapter.apply {
                type = ResultAdapter.AdapterType.SEARCH
            }
        }
    }

    override fun clickItem(data: Documents) {
        viewModel.insertMyItem(data)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment = SearchFragment()
    }
}