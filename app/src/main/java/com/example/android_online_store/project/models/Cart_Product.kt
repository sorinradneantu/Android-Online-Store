package com.example.android_online_store.project.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart_Product(val user_id: String = "",val product_owner_id:String = "", val product_id:String = "", val product_name: String = "", val price:String = "", val image: String = "", var cart_quantity:String = "", var prod_quantity: String = "", var id:String = "" ) :
    Parcelable