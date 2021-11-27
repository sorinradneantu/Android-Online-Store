package com.example.android_online_store.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.android_online_store.R
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Product

class ProductWindowActivity : AppCompatActivity() {

    private var prodId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_window)

        if(intent.hasExtra("ProductID")){
            prodId = intent.getStringExtra("ProductID")!!
        }

        var productOwnerId: String = ""

        if(intent.hasExtra("OwnerID")){
            productOwnerId = intent.getStringExtra("OwnerID")!!
        }

        if(FirestoreController().getId() == productOwnerId){
            findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.GONE
            findViewById<Button>(R.id.btn_edit).visibility = View.VISIBLE
        }else{
            findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.VISIBLE
            findViewById<Button>(R.id.btn_edit).visibility = View.GONE
        }


        getProdDetails()

        val editButton = findViewById<Button>(R.id.btn_edit)
        editButton.setOnClickListener { editProduct() }

    }

    fun getDetailsSuccess(product: Product){
        GlideLoader(this@ProductWindowActivity).loadProdImage(product.image,findViewById<ImageView>(R.id.iv_product_detail_image))

        findViewById<TextView>(R.id.tv_product_name).text = product.product_name
        findViewById<TextView>(R.id.tv_product_price).text = "${product.price} $"
        findViewById<TextView>(R.id.tv_product_description).text = product.description
        findViewById<TextView>(R.id.tv_product_quantity).text = product.quantity

    }

    private fun getProdDetails(){
        FirestoreController().getProductDetails(this, prodId)
    }

    // TODO : Edit product functionality
    private fun editProduct(){
        Toast.makeText(this, "TO DO : Edit Item", Toast.LENGTH_SHORT).show();
    }



}