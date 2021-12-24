package com.example.android_online_store.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.adapters.MyOrdersAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.Order

class MyOrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        getOrdersList()

    }


    fun getOrdersInUI(ordersList: ArrayList<Order>){

        if(ordersList.size > 0){
            findViewById<RecyclerView>(R.id.rv_my_order_items).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_orders_found).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rv_my_order_items).layoutManager = LinearLayoutManager(this@MyOrdersActivity)
            findViewById<RecyclerView>(R.id.rv_my_order_items).setHasFixedSize(true)

            val ordersListAdapter = MyOrdersAdapter(this@MyOrdersActivity, ordersList)
            findViewById<RecyclerView>(R.id.rv_my_order_items).adapter = ordersListAdapter


        }else{

            findViewById<RecyclerView>(R.id.rv_my_order_items).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_orders_found).visibility = View.VISIBLE

        }

    }

    private fun getOrdersList(){
        FirestoreController().getOrders(this@MyOrdersActivity)
    }

    override fun onResume() {
        super.onResume()

    }

    fun getOrdersFail(){

    }

}