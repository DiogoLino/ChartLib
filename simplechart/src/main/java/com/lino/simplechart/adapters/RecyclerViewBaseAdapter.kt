package com.lino.simplechart.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

abstract class RecyclerViewBaseAdapter<in I, S: Context, VH : RecyclerViewBaseAdapter.RecyclerViewBaseViewHolder<I, S>>(
        protected val screen: S,
        private val items: List<I>)
    : RecyclerView.Adapter<VH>() {

    abstract fun getItemLayoutRscId(): Int
    abstract fun createViewHolder(screen: S, view: View): VH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(screen).inflate(getItemLayoutRscId(), parent, false)
        return createViewHolder(screen, v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], position)
    }

    abstract class RecyclerViewBaseViewHolder<in I, S: Context>(val screen: S, override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        abstract fun bind(item: I, position: Int)

    }
}