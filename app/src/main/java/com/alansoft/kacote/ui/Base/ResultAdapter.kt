package com.alansoft.kacote.ui.Base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.ImageDocuments
import com.alansoft.kacote.databinding.ImageItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop


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

    var type: AdapterType = AdapterType.SEARCH

    override fun createBinding(parent: ViewGroup): ImageItemBinding {
        val binding = DataBindingUtil.inflate<ImageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.image_item,
            parent,
            false
        )
        binding.root.setOnClickListener {
            AlertDialog.Builder(binding.root.context).run {
                setTitle(if (type == AdapterType.SEARCH) "저장 하시겠습니까?" else "삭제 하시겠습니까?")
                setMessage("선택하세요.")
                setPositiveButton(
                    if (type == AdapterType.SEARCH) "저장" else "취소"
                ) { dialog, id ->
                }
                setNegativeButton(
                    if (type == AdapterType.SEARCH) "취소" else "삭제"
                ) { dialog, id ->
                }
                create()
            }.show()
        }
        return binding
    }

    override fun bind(binding: ImageItemBinding, item: ImageDocuments) {
        binding.run {
            description.text = item.doc_url
            title.text = item.collection
            subTitle.text = item.display_sitename
            Glide.with(root)
                .load(item.thumbnail_url)
                .transform(CenterCrop())
                .into(thumbnail)
        }
    }

    enum class AdapterType {
        SEARCH,
        MY
    }
}