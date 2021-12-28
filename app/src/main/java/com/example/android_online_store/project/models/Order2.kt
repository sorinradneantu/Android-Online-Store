package com.example.android_online_store.project.models

import android.os.Parcelable
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.parcel
import kotlinx.parcelize.Parcelize

/**
 * A data model class for Order item with required fields.
 */
@Parcelize
data class Order2(
    val user_id: String = "",
    val items: ArrayList<Cart_Product> = ArrayList(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    var id: String = ""
) : Parcelable