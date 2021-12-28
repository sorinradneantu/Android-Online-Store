package com.example.android_online_store.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.adapters.CartListAdapter
import com.example.android_online_store.project.models.Order
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        var myOrderDetails : Order = Order()
        if(intent.hasExtra("OrderDetailsExtra")){
            myOrderDetails = (intent.getParcelableExtra("OrderDetailsExtra"))!!
        }

        setupOrderDetails(myOrderDetails)


    }


    private fun setupOrderDetails(orderDetails: Order){
        findViewById<TextView>(R.id.tv_order_details_id).text = orderDetails.details

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.date
        val orderDate = formatter.format(calendar.time)

        findViewById<TextView>(R.id.tv_order_details_date).text = orderDate
        findViewById<TextView>(R.id.tv_order_status).text = "Delivered"

        findViewById<RecyclerView>(R.id.rv_my_order_items_list).layoutManager = LinearLayoutManager(this@OrderDetailsActivity)
        findViewById<RecyclerView>(R.id.rv_my_order_items_list).setHasFixedSize(true)

        val cartListAdapter = CartListAdapter(this@OrderDetailsActivity, orderDetails.items, false)
        findViewById<RecyclerView>(R.id.rv_my_order_items_list).adapter = cartListAdapter

        val sharedPreferences = this.getSharedPreferences("shopPreferences", MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname_logged", "")
        val lastname = sharedPreferences.getString("lastname_logged", "")
        val email = sharedPreferences.getString("email_logged", "")
        val phoneNumber = sharedPreferences.getString("phoneNr_logged", "")

        findViewById<TextView>(R.id.tv_order_details_address).text = orderDetails.address
        findViewById<TextView>(R.id.tv_order_details_full_name).text = firstname + " " + lastname
        findViewById<TextView>(R.id.tv_order_details_email).text = email
        findViewById<TextView>(R.id.tv_order_details_mobile_number).text = phoneNumber

        findViewById<TextView>(R.id.tv_order_details_shipping_charge).text = orderDetails.shipping_charge
        findViewById<TextView>(R.id.tv_order_details_sub_total).text = orderDetails.sub_total_amount
        findViewById<TextView>(R.id.tv_order_details_total_amount).text = orderDetails.total_amount
    }

}