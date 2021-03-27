package com.alansoft.kacote.ui.main

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
            viewPager.isUserInputEnabled = false
            viewPager.adapter = object : FragmentStateAdapter(this@MainFragment) {
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        1 -> MyFragment.newInstance()
                        else -> SearchFragment.newInstance()
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

        initSearchInputListener()
    }

    private fun initSearchInputListener() {
        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = binding.input.text.toString()
        dismissKeyboard(v.windowToken)
        childFragmentManager.setFragmentResult("requestKey", bundleOf("query" to query))
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)