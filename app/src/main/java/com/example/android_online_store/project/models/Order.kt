package com.example.android_online_store.project.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart_Product> = ArrayList(),
    val address: String = "",
    val details: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val date: Long = 0L,
    var id: String = ""

) : Parcelable