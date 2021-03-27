package com.alansoft.kacote.ui.Base

import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.alansoft.kacote.R
import com.alansoft.kacote.data.model.Documents
import com.alansoft.kacote.data.model.ImageDocuments
import com.alansoft.kacote.data.model.VClipDocuments
import com.alansoft.kacote.databinding.ImageItemBinding
import com.alansoft.kacote.databinding.VclipItemBinding
import com.alansoft.kacote.utils.TabType
import com.alansoft.kacote.utils.ViewType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop


/**
 * Created by LEE MIN KYU on 2021/03/27
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
class ResultAdapter(
    private val itemCallback: ((Documents) -> Unit)?
) :
    BaseListAdapter<Documents>(object :
        DiffUtil.ItemCallback<Documents>() {
        override fun areItemsTheSame(
            oldItem: Documents,
            newItem: Documents
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Documents,
            newItem: Documents
        ): Boolean {
            return oldItem.datetime == newItem.datetime
        }
    }) {

    var type: TabType = TabType.SEARCH_RESULT

    override fun createView(viewType: Int): Int {
        return when (viewType) {
            ViewType.IMAGE.hashCode() -> R.layout.image_item
            else -> R.layout.vclip_item
        }
    }

    override fun getViewType(item: Documents): Int {
        return if (item is ImageDocuments) {
            ViewType.IMAGE.hashCode()
        } else {
            ViewType.VCLIP.hashCode()
        }
    }

    override fun bind(binding: ViewDataBinding, item: Documents) {
        if (item is ImageDocuments && binding is ImageItemBinding) {
            binding.run {
                description.text = item.doc_url
                title.text = item.collection
                subTitle.text = item.datetime.toString()
                Glide.with(root)
                    .load(item.thumbnail_url)
                    .transform(CenterCrop())
                    .into(thumbnail)
            }
        } else if (item is VClipDocuments && binding is VclipItemBinding) {
            binding.run {
                description.text = item.title
                author.text = item.author
                subTitle.text = item.datetime.toString()
                Glide.with(root)
                    .load(item.thumbnail)
                    .transform(CenterCrop())
                    .into(thumbnail)
            }
        }

        binding.root.setOnClickListener {
            AlertDialog.Builder(binding.root.context).run {
                setTitle(if (type == TabType.SEARCH_RESULT) "저장 하시겠습니까?" else "삭제 하시겠습니까?")
                setMessage("선택하세요.")
                setPositiveButton(
                    if (type == TabType.SEARCH_RESULT) "저장" else "삭제"
                ) { dialog, id ->
                    itemCallback?.invoke(item)
                }
                setNegativeButton(
                    "취소"
                ) { dialog, id ->

                }
                create()
            }.show()
        }
    }
}