package com.alansoft.kacote.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alansoft.kacote.databinding.FragmentSearchBinding
import com.alansoft.kacote.ui.main.PageViewModel
import com.alansoft.kacote.ui.main.PlaceholderFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: Int): SearchFragment {
            return SearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java).apply {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSearchBinding.inflate(inflater, container, false).root

    }

}