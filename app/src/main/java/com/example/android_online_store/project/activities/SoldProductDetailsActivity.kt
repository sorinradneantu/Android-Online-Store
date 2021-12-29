package com.example.android_online_store.project.activities

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.android_online_store.R
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.SoldProduct
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)


        var productDetails: SoldProduct = SoldProduct()

        if(intent.hasExtra("SoldItems")){
            productDetails = intent.getParcelableExtra<SoldProduct>("SoldItems")!!
        }

        setup(productDetails)

    }

    private fun setup(productDetails: SoldProduct){

        findViewById<TextView>(R.id.tv_sold_product_details_id).text = productDetails.order_id

        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        findViewById<TextView>(R.id.tv_sold_product_details_date).text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductDetailsActivity).loadProdImage(productDetails.image, findViewById<ImageView>(R.id.iv_product_item_image))

        findViewById<TextView>(R.id.tv_product_item_name).text = productDetails.title
      //  findViewById<TextView>(R.id.tv_product_item_name).text = productDetails.title
        findViewById<TextView>(R.id.tv_product_item_price).text = "${productDetails.price} $"
        findViewById<TextView>(R.id.tv_sold_product_quantity).text = productDetails.sold_quantity

        findViewById<TextView>(R.id.tv_sold_details_address).text = productDetails.address

        findViewById<TextView>(R.id.tv_sold_product_total_amount).text = productDetails.total_amount
        findViewById<TextView>(R.id.tv_sold_product_shipping_charge).text = productDetails.shipping_charge
        findViewById<TextView>(R.id.tv_sold_product_sub_total).text = productDetails.sub_total_amount
    }
}