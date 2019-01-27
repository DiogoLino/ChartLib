package com.lino.simplechart.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class ChartItem(var firstItemValue: BigDecimal? = null,
                var secondItemValue: BigDecimal? = null,
                var date : String? = null) : Parcelable {
}