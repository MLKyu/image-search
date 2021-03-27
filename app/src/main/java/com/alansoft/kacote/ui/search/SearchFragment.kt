package com.alansoft.kacote.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alansoft.kacote.R
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.databinding.FragmentSearchBinding
import com.alansoft.kacote.ui.Base.ResultAdapter
import com.alansoft.kacote.utils.autoCleared
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var binding by autoCleared<FragmentSearchBinding>()
    private val viewModel: SearchViewModel by viewModels()

    private var adapter by autoCleared<ResultAdapter>()

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return binding.root
    }

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
                        binding.emptyResult = result.data?.meta?.pageable_count == 0
                        result.data?.documents?.let {
                            adapter.submitList(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, result.message.toString(), Toast.LENGTH_LONG)
                            .show()
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
                        Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }

        val resultAdapter = ResultAdapter()
        binding.serachRv.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == adapter?.itemCount ?: 0 - 1) {
                        viewModel.loadNextPage()
                    }
                }
            })
            adapter = resultAdapter
        }
        adapter = resultAdapter
    }
}