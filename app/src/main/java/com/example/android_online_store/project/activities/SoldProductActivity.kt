package com.example.android_online_store.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.adapters.SoldProductsAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.SoldProduct

class SoldProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product)

        getSoldProductList()
    }

    private fun getSoldProductList(){
        FirestoreController().getSoldProductsList(this@SoldProductActivity)
    }

    fun successSoldProductsList(soldProductList: ArrayList<SoldProduct>){
        if(soldProductList.size > 0){

            findViewById<RecyclerView>(R.id.rv_sold_product_items).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_sold_products_found).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rv_sold_product_items).layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_sold_product_items).setHasFixedSize(true)

            val spAdapter = SoldProductsAdapter(this@SoldProductActivity, soldProductList)
            findViewById<RecyclerView>(R.id.rv_sold_product_items).adapter = spAdapter

        }else{
            findViewById<RecyclerView>(R.id.rv_sold_product_items).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_sold_products_found).visibility = View.VISIBLE
        }
    }

    fun failSoldProductsList(){

    }

}