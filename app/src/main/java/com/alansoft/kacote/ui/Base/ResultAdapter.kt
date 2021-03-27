package com.alansoft.kacote.ui.Base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.ImageDocuments
import com.alansoft.kacote.databinding.ImageItemBinding

/**
 * Created by LEE MIN KYU on 2021/03/27
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class ResultAdapter :
    BaseListAdapter<ImageDocuments, ImageItemBinding>(object :
        DiffUtil.ItemCallback<ImageDocuments>() {
        override fun areItemsTheSame(
            oldItem: ImageDocuments,
            newItem: ImageDocuments
        ): Boolean {
            return oldItem.collection == newItem.collection
                    && oldItem.display_sitename == newItem.display_sitename
        }

        override fun areContentsTheSame(
            oldItem: ImageDocuments,
            newItem: ImageDocuments
        ): Boolean {
            return oldItem.doc_url == newItem.doc_url
                    && oldItem.datetime == newItem.datetime
        }
    }) {
    override fun createBinding(parent: ViewGroup): ImageItemBinding {
        val binding = DataBindingUtil.inflate<ImageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.image_item,
            parent,
            false
        )
//        binding.showFullName = showFullName

        binding.root.setOnClickListener {
//            binding.repo?.let {
//                repoClickCallback?.invoke(it)
//            }
        }
        return binding
    }

    override fun bind(binding: ImageItemBinding, item: ImageDocuments) {
//        binding.repo = item

        binding.description.text = item.doc_url
        binding.title.text = item.collection
        binding.subTitle.text = item.display_sitename
    }
}