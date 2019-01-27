package com.lino.simplechart.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import com.lino.simplechart.R
import com.lino.simplechart.adapters.ChartAdapter
import com.lino.simplechart.entities.ChartItem
import kotlinx.android.synthetic.main.activity_main.view.*
import java.math.BigDecimal
import java.util.*


class ChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_GRAPH_VALUE = "0"
    }

    private var color1: Int = 0
    private var color2: Int = 0
    private var chartAdapter: ChartAdapter? = null
    private var maxScaleHeight: Int = 0

    init {
        createLayout(context)
        applyAttributes(context, attrs)
    }

    private fun createLayout(context: Context?) {
        LayoutInflater.from(context).inflate(R.layout.activity_main, this, true)

    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChartView)

        try {
            // apply attrs to views
            val bar1DefaultColor = ContextCompat.getColor(context, R.color.bar1DefaultColor)
            val bar2DefaultColor = ContextCompat.getColor(context, R.color.bar2DefaultColor)

            setBar1StatusText(typedArray.getString(R.styleable.ChartView_IFV_bar1StatusText))
            setBar2StatusText(typedArray.getString(R.styleable.ChartView_IFV_bar2StatusText))

            color1 = typedArray.getColor(
                    R.styleable.ChartView_IFV_bar1Color,
                    bar1DefaultColor
            )
            color2 = typedArray.getColor(
                    R.styleable.ChartView_IFV_bar2Color,
                    bar2DefaultColor
            )

            if (!isInEditMode) {
                setBar1Color(color1)
                setBar2Color(color2)
            }

        } finally {
            typedArray.recycle()
        }
    }

    // attrs

    private fun setBar1StatusText(text: String) {
        bar1Text.text = text
    }

    private fun setBar2StatusText(text: String) {
        bar2Text.text = text
    }

    private fun setBar1Color(color: Int) {
        barStatus1.setBackgroundColor(color)
    }

    private fun setBar2Color(color: Int) {
        barStatus2.setBackgroundColor(color)
    }


    private fun initChartAdapter(measureHeight: Int, chartItemsList: ArrayList<ChartItem>,
                                 onItemClicked : (ChartItem) -> Unit) {
        val chartRecyclerManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
        )
        recycler.layoutManager = chartRecyclerManager

        chartAdapter =
                ChartAdapter(context, chartItemsList, measureHeight, color1, color2, maxScaleHeight,onItemClicked)
        recycler.adapter = chartAdapter
    }

    private fun setScale(items: ArrayList<ChartItem>, currency: String) {
        val maxValue = getMaxHeight(items)

        var x = 0.0
        val value = maxValue.toInt()
        while (x < value) {
            val x1 = x + 1500

            if (value < x1) {
                x = x1
                break
            }
            x = x1
        }

        maxScaleHeight = x.toInt()

        rootView.scaleText1.text =
                context.getString(R.string.chart_scale_value, x.toString(), currency)
        rootView.scaleText2.text =
                context.getString(R.string.chart_scale_value, (x * 2 / 3).toString(), currency)
        rootView.scaleText3.text =
                context.getString(R.string.chart_scale_value, (x / 3).toString(), currency)
        rootView.scaleText4.text =
                context.getString(R.string.chart_scale_value, DEFAULT_GRAPH_VALUE, currency)

    }


    fun drawChart(chartItemsList: ArrayList<ChartItem>, currency: String, onItemClicked : (ChartItem) -> Unit) {
        setScale(chartItemsList, currency)
        measureLayout.afterMeasured {
            initChartAdapter(
                    measuredHeight - resources.getDimensionPixelSize(R.dimen.layout_spacing),
                    chartItemsList, onItemClicked
            )
        }
    }

    private fun getMaxHeight(items: ArrayList<ChartItem>): BigDecimal {
        var maxValue = BigDecimal.ZERO
        items.forEach {
            if (it.secondItemValue != null && it.secondItemValue!! > maxValue) {
                maxValue = it.secondItemValue!!
            }
            if (it.firstItemValue != null && it.firstItemValue!! > maxValue) {
                maxValue = it.firstItemValue!!
            }
        }
        return maxValue
    }

    private inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }

}