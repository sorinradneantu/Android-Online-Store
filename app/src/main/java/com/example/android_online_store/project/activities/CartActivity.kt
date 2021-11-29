package com.example.android_online_store.project.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.adapters.CartListAdapter
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.Cart_Product
import com.example.android_online_store.project.models.Product

class CartActivity : AppCompatActivity() {

   private lateinit var  mProdList: ArrayList<Product>
   private lateinit var mCartProdList: ArrayList<Cart_Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
    }

    override fun onResume() {
        super.onResume()
        getProdList()
    }

    private fun getCartProductsFromDB(){
        FirestoreController().getCartProducts(this@CartActivity)
    }

    @SuppressLint("CutPasteId")
    fun getCartProductsSuccessfully(prodList: ArrayList<Cart_Product>){

        //val rv: RecyclerView = binding.rvMyProductItems
        //val tv: TextView = binding.tvNoProductsFound

        for(prod in mProdList){
            for(cartProd in prodList){
                if(prod.prod_id == cartProd.product_id){
                    cartProd.prod_quantity = prod.quantity
                    if(prod.quantity.toInt() == 0){
                        cartProd.cart_quantity = prod.quantity
                    }
                }
            }
        }

        mCartProdList = prodList

        if(mCartProdList.size > 0){

            findViewById<RecyclerView>(R.id.rv_cart_items_list).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_cart_item_found).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rv_cart_items_list).layoutManager = LinearLayoutManager(this@CartActivity)
            findViewById<RecyclerView>(R.id.rv_cart_items_list).setHasFixedSize(true)
            val cartListAdapter = CartListAdapter(this@CartActivity, prodList)
            findViewById<RecyclerView>(R.id.rv_cart_items_list).adapter = cartListAdapter

            var price: Double = 0.0
            for(product in mCartProdList){
                val availableQuantity = product.prod_quantity.toInt()

                if(availableQuantity > 0){
                    val p = product.price.toDouble()
                    val q = product.cart_quantity.toInt()
                    price = price + (p * q)
                }
            }

            findViewById<TextView>(R.id.tv_price).text = "${price} $"
            findViewById<TextView>(R.id.tv_shipping).text = "12.0 $"

            if(price > 0) {

                findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.VISIBLE

                val total = price + 12
                findViewById<TextView>(R.id.tv_total).text = "${total} $"

            }else{
                findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.GONE
            }
        }else{
            findViewById<RecyclerView>(R.id.rv_cart_items_list).visibility = View.GONE
            findViewById<LinearLayout>(R.id.checkout_layout).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_cart_item_found).visibility = View.VISIBLE
        }

    }

    fun getCartProductsFailed(){

    }

    fun getAllProductsFromDBSuccessfully(products: ArrayList<Product>){
        mProdList = products
        getCartProductsFromDB()
    }

    fun getProdList(){
        FirestoreController().getAllProductsList(this)
    }

    fun productRemovedFromCartSuccessfully(){
        Toast.makeText(this, "The product was removed from cart !", Toast.LENGTH_SHORT).show()
        getCartProductsFromDB()
    }

    fun productRemovedFromCartFailed(){
        Toast.makeText(this, "The product was not removed from cart !", Toast.LENGTH_SHORT).show()
    }

    fun updateCartSuccessfully(){
        getCartProductsFromDB()
    }

    fun updateCartFailed(){

    }

    fun showQuantityError(){
        Toast.makeText(this, "This quantity is not available !", Toast.LENGTH_SHORT).show()
    }
}