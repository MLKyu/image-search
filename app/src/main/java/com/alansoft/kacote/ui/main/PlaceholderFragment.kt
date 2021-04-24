package com.alansoft.kacote.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.ui.base.ResultAdapter
import com.alansoft.kacote.utils.autoCleared

/**
 * Created by LEE MIN KYU on 2021/03/24
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
abstract class PlaceholderFragment<B : ViewDataBinding>(private val layoutId: Int) :
    Fragment() {
    var binding by autoCleared<B>()
    var adapter by autoCleared<ResultAdapter>()

    abstract fun clickItem(data: Documents)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ResultAdapter { item ->
            clickItem(item)
        }
    }
}