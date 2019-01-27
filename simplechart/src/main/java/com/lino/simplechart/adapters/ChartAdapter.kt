package com.lino.simplechart.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import com.lino.simplechart.R
import com.lino.simplechart.entities.ChartItem
import kotlinx.android.synthetic.main.chart_item.*
import kotlinx.android.synthetic.main.chart_item.view.*
import java.util.*


class ChartAdapter(
        screen: Context,
        items: ArrayList<ChartItem>,
        val measureHeight: Int,
        private val color1: Int,
        private val color2: Int,
        val maxValue: Int,
        private val onItemClickedAction: ((ChartItem) -> Unit)? = null)
    : RecyclerViewBaseAdapter<ChartItem, Context, ChartAdapter.ChartViewHolder>(screen, items) {

    override fun getItemLayoutRscId() = R.layout.chart_item

    override fun createViewHolder(screen: Context, view: View) = ChartViewHolder(screen, view)


    inner class ChartViewHolder(screen: Context, view: View) :
            RecyclerViewBaseAdapter.RecyclerViewBaseViewHolder<ChartItem, Context>(screen, view) {


        override fun bind(item: ChartItem, position: Int) {
            date.text = item.date
            // calculate 1bar height
            val despenseHeight = ((measureHeight * item.firstItemValue?.toInt()!!) / maxValue)
            // calculate 2bar height
            val receiptHeight = ((measureHeight * item.secondItemValue?.toInt()!!) / maxValue)


            containerView.bar1.layoutParams.height = despenseHeight
            containerView.bar2.layoutParams.height = receiptHeight

            containerView.bar1.setOnClickListener { onBarItemItemClicked(onItemClickedAction, item) }
            containerView.bar2.setOnClickListener { onBarItemItemClicked(onItemClickedAction, item) }


            // apply bars colors
            getBackgroundDrawable(bar1, bar2)
        }
    }


    fun getBackgroundDrawable(bar1: View, bar2: View) {
        val background1 = bar1.background
        val background2 = bar2.background
        when (background1) {
            is ShapeDrawable -> // cast to 'ShapeDrawable'
                background1.paint.color = color1
            is GradientDrawable -> {
                // cast to 'GradientDrawable'
                background1.setColor(color1)
            }
            is ColorDrawable -> {
                // alpha value may need to be set again after this call
                background1.color = color1
            }
        }
        when (background2) {
            is ShapeDrawable -> // cast to 'ShapeDrawable'
                background2.paint.color = color2
            is GradientDrawable -> {
                // cast to 'GradientDrawable'
                background2.setColor(color2)
            }
            is ColorDrawable -> {
                // alpha value may need to be set again after this call
                background2.color = color2
            }
        }
    }

    private fun onBarItemItemClicked(onItemClickedAction: ((ChartItem) -> Unit)?,
                                     item: ChartItem) {
        onItemClickedAction?.invoke(item)
    }
}
