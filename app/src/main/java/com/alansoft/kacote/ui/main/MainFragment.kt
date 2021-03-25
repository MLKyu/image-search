package com.alansoft.kacote.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alansoft.kacote.R
import com.alansoft.kacote.databinding.MainFragmentBinding
import com.alansoft.kacote.ui.my.MyFragment
import com.alansoft.kacote.ui.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created by LEE MIN KYU on 2021/03/25
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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