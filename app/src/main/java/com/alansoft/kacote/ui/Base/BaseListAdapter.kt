package com.alansoft.kacote.ui.Base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by LEE MIN KYU on 2021/03/26
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
abstract class BaseListAdapter<M>(diffCallback: DiffUtil.ItemCallback<M>) :
    ListAdapter<M, BaseViewHolder>(AsyncDifferConfig.Builder<M>(diffCallback).build()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val resourceId = createView(viewType)
        return BaseViewHolder(parent, resourceId)
    }

    protected abstract fun createView(viewType: Int): Int

    override fun getItemViewType(position: Int): Int {
        return getViewType(getItem(position))
    }

    protected abstract fun getViewType(item: M): Int

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        DataBindingUtil.bind<ViewDataBinding>(holder.itemView)?.let {
            bind(it, getItem(position))
            it.executePendingBindings()
        }
    }

    protected abstract fun bind(binding: ViewDataBinding, item: M)
}

class BaseViewHolder(parent: ViewGroup, @LayoutRes layout: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layout, parent, false)
)