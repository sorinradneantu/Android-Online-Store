package com.example.android_online_store.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.adapters.CartListAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.Cart_Product
import com.example.android_online_store.project.models.Order
import com.example.android_online_store.project.models.Product
import com.google.firebase.storage.FirebaseStorage

class CheckoutActivity : AppCompatActivity() {

    private lateinit var mProductsList : ArrayList<Product>
    private lateinit var mCartProductsList : ArrayList<Cart_Product>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val sharedPreferences = this.getSharedPreferences("shopPreferences", MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname_logged", "")
        val lastname = sharedPreferences.getString("lastname_logged", "")
        val email = sharedPreferences.getString("email_logged", "")
        val address = sharedPreferences.getString("address_logged", "")
        val phoneNumber = sharedPreferences.getString("phoneNr_logged", "")

        val checkoutFullName = findViewById<TextView>(R.id.tv_checkout_full_name);
        val checkoutAddress = findViewById<TextView>(R.id.tv_checkout_address)
        val checkoutPhoneNumber = findViewById<TextView>(R.id.tv_checkout_mobile_number)
        val checkoutEmail = findViewById<TextView>(R.id.tv_checkout_email)

        checkoutFullName.text = firstname + " " + lastname
        checkoutAddress.text = address
        checkoutPhoneNumber.text = phoneNumber
        checkoutEmail.text = email

        getProductList()

        findViewById<Button>(R.id.btn_checkout_finish_order).setOnClickListener{
            placeOrder()
        }

    }

    private fun getProductList(){

        FirestoreController().getAllProductsList(this@CheckoutActivity)

    }

    fun succestGetProductList(productList: ArrayList<Product>){

        mProductsList = productList
        getCartItemsList()

    }

    private fun getCartItemsList(){
        FirestoreController().getCartProducts(this@CheckoutActivity)
    }

    fun successGetCartItemsList(cartList : ArrayList<Cart_Product>){

        for(product in mProductsList){
            for(cartItem in cartList) {
                if(product.prod_id == cartItem.product_id){
                    cartItem.prod_quantity = product.quantity
                }
            }
        }

        mCartProductsList = cartList

        findViewById<RecyclerView>(R.id.rv_cart_list_items).layoutManager = LinearLayoutManager(this@CheckoutActivity)
        findViewById<RecyclerView>(R.id.rv_cart_list_items).setHasFixedSize(true)

        val cartListAdapter = CartListAdapter(this@CheckoutActivity, mCartProductsList, false)
        findViewById<RecyclerView>(R.id.rv_cart_list_items).adapter = cartListAdapter


        for(item in mCartProductsList){
            val availableQ = item.prod_quantity.toInt()
            if(availableQ > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }

        findViewById<TextView>(R.id.tv_checkout_sub_total).text = "${mSubTotal} $"
        findViewById<TextView>(R.id.tv_checkout_shipping_charge).text = "12.0 $"

        if(mSubTotal > 0){
            findViewById<LinearLayout>(R.id.ll_checkout_place_order).visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 12.0
            findViewById<TextView>(R.id.tv_checkout_total_amount).text = "${mTotalAmount} $"
        }else{
            findViewById<LinearLayout>(R.id.ll_checkout_place_order).visibility = View.GONE
        }

    }

    fun failedGetCartItemsList(){

    }

    private fun placeOrder(){

        val sharedPreferences = this.getSharedPreferences("shopPreferences", MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname_logged", "")
        val lastname = sharedPreferences.getString("lastname_logged", "")
        val email = sharedPreferences.getString("email_logged", "")
        val address = sharedPreferences.getString("address_logged", "")
        val phoneNumber = sharedPreferences.getString("phoneNr_logged", "")

        val order = Order(
            FirestoreController().getId(),
            mCartProductsList,
            address!!,
            "Order : ${System.currentTimeMillis()}",
            mCartProductsList[0].image,
            mSubTotal.toString(),
            "12.0 $",
            mTotalAmount.toString()
        )

        FirestoreController().placeOrder(this, order);

    }

    fun placeOrderSuccessfully(){

        Toast.makeText(this, "The order was placed successfully", Toast.LENGTH_SHORT).show();

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun placeOrderFailed(){
        Toast.makeText(this, "The order was not placed !", Toast.LENGTH_SHORT).show();
    }

}