package com.alansoft.kacote.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alansoft.kacote.R
import com.alansoft.kacote.databinding.MainFragmentBinding
import com.alansoft.kacote.ui.my.MyFragment
import com.alansoft.kacote.ui.search.SearchFragment
import com.alansoft.kacote.utils.autoCleared
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var binding by autoCleared<MainFragmentBinding>()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.main_fragment,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.let { viewPager ->
            viewPager.adapter = object : FragmentStateAdapter(this@MainFragment) {
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        1 -> MyFragment.newInstance(position)
                        else -> SearchFragment.newInstance(position)
                    }
                }

                override fun getItemCount(): Int {
                    return 2
                }
            }

            TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
                tab.text = context?.resources?.getString(TAB_TITLES[position])
            }.attach()
        }
    }
}

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)