package com.alansoft.kacote.ui.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.utils.Resource
import com.alansoft.kacote.databinding.FragmentMyBinding
import com.alansoft.kacote.ui.main.PlaceholderFragment
import com.alansoft.kacote.utils.TabType
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class MyFragment : PlaceholderFragment<FragmentMyBinding>(R.layout.fragment_my) {

    private val viewModel: MyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.run {
            results.observe(viewLifecycleOwner, { result ->
                when (result.status) {
                    Resource.Status.SUCCESS -> {
                        result.data.let {
                            binding.emptyResult = it.isNullOrEmpty()
                            adapter.submitList(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.emptyResult = true
                    }
                    else -> {

                    }
                }
            })
        }

        binding.myRv.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == adapter?.itemCount ?: 0 - 1) {

                    }
                }
            })

            adapter = this@MyFragment.adapter.apply {
                type = TabType.MY
            }
        }
    }

    override fun clickItem(data: Documents) {
        viewModel.deleteItem(data)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyFragment()
    }
}