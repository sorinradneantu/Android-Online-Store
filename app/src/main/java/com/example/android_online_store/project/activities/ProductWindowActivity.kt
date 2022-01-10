package com.example.android_online_store.project.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.android_online_store.R
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Cart_Product
import com.example.android_online_store.project.models.Product
import com.example.android_online_store.project.models.User
import com.google.android.gms.common.api.Response
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import org.w3c.dom.Text

class ProductWindowActivity : AppCompatActivity() {

    private lateinit var prodDetails: Product

    private var prodId: String = ""

    var productOwnerId: String = ""

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_window)

        if(intent.hasExtra("ProductID")){
            prodId = intent.getStringExtra("ProductID")!!
        }


        if(intent.hasExtra("OwnerID")){
            productOwnerId = intent.getStringExtra("OwnerID")!!
        }


        //var curOwnerGrade: String = ""
        val docRef = db.collection("users").document(productOwnerId)
        docRef.get().addOnSuccessListener { document ->
            if(document != null){
                //Log.d(TAG,"DocumentSnapshot data: ${document.data?.get("grade")}")
                val currentOwnerGrade = document.data?.get("grade")
                findViewById<TextView>(R.id.tv_owner_grade).text = currentOwnerGrade.toString()
                //curOwnerGrade = currentOwnerGrade.toString()
            }else{
                //Log.d(TAG,"no such document")
            }

        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ",exception)
        }




        if(FirestoreController().getId() == productOwnerId){
            findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.GONE
            findViewById<Button>(R.id.btn_edit).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_sendGrade).visibility = View.GONE
            findViewById<Button>(R.id.sendGradeButton).visibility = View.GONE
        }else{
            findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.VISIBLE
            findViewById<Button>(R.id.btn_edit).visibility = View.GONE
            findViewById<TextView>(R.id.tv_sendGrade).visibility = View.VISIBLE
            findViewById<Button>(R.id.sendGradeButton).visibility = View.VISIBLE
        }


        getProdDetails()

        val editButton = findViewById<Button>(R.id.btn_edit)
        editButton.setOnClickListener { editProduct() }

        val addToCartButton = findViewById<Button>(R.id.btn_add_product_to_cart)
        addToCartButton.setOnClickListener {addProductToCart()}

        val sendGradeButton = findViewById<Button>(R.id.sendGradeButton)
        sendGradeButton.setOnClickListener { sendGrade() }

        findViewById<TextView>(R.id.tv_already_exist).visibility = View.GONE

    }

    fun getDetailsSuccess(product: Product){
        GlideLoader(this@ProductWindowActivity).loadProdImage(product.image,findViewById<ImageView>(R.id.iv_product_detail_image))

        findViewById<TextView>(R.id.owner_name).text = product.owner_name
        findViewById<TextView>(R.id.tv_product_name).text = product.product_name
        findViewById<TextView>(R.id.tv_product_price).text = "${product.price} $"
        findViewById<TextView>(R.id.tv_product_description).text = product.description
        findViewById<TextView>(R.id.tv_product_quantity).text = product.quantity

        if(product.quantity.toInt() == 0){
            findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.GONE
            findViewById<TextView>(R.id.tv_product_quantity).text = "Product not available"
        }else{
            if(FirestoreController().getId() != product.owner_id){
                FirestoreController().checkProductExistInCart(this, prodId)
            }
        }

        prodDetails = product


    }

    private fun getProdDetails(){
        FirestoreController().getProductDetails(this, prodId)
    }

    // TODO : Edit product functionality
    private fun editProduct(){
        Toast.makeText(this, "TO DO : Edit Item", Toast.LENGTH_SHORT).show();
    }
/*
    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_add_product_to_cart -> {
                    addProductToCart()
                }
            }
        }
    }
*/
    private fun addProductToCart(){

        val cart_prod = Cart_Product(FirestoreController().getId(),productOwnerId, prodId, prodDetails.product_name, prodDetails.price, prodDetails.image, "1")
        FirestoreController().addProductToCart(this, cart_prod)

    }

    fun productAddedToCartSuccessfully(){
        Toast.makeText(this, "The product was added to cart successfully!", Toast.LENGTH_SHORT).show();

        findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.GONE
        findViewById<TextView>(R.id.tv_already_exist).visibility = View.VISIBLE
    }

    fun productAddedToCartFailed(){
        Toast.makeText(this, "The product was not added to cart !", Toast.LENGTH_SHORT).show();
    }

    fun prodExistAlreadyInCart(){
        findViewById<Button>(R.id.btn_add_product_to_cart).visibility = View.GONE
        findViewById<TextView>(R.id.tv_already_exist).visibility = View.VISIBLE
    }

    fun sendGrade(){

        if(findViewById<TextView>(R.id.tv_sendGrade).text.isNullOrEmpty()){
            Toast.makeText(this, "Empty field !", Toast.LENGTH_SHORT).show();
        }else{
            val newGrade: Double = findViewById<TextView>(R.id.tv_sendGrade).text.toString().toDouble()
            if(newGrade > 0 && newGrade<=10) {
                val itemHashMap = HashMap<String, Any>()

                val docRef = db.collection("users").document(productOwnerId)
                docRef.get().addOnSuccessListener { document ->
                    if(document != null){
                        val currentOwnerGrade = document.data?.get("grade")

                        itemHashMap["grade"] = ((newGrade + currentOwnerGrade.toString().toDouble())/2).toString()
                        FirestoreController().updateGrade(this@ProductWindowActivity,productOwnerId, itemHashMap)
                    }else{

                    }
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ",exception)
                }

               //itemHashMap["grade"] = newGrade

               //FirestoreController().updateGrade(this@ProductWindowActivity,productOwnerId, itemHashMap)
            }else{
                Toast.makeText(this, "The grade must be between 1 and 10", Toast.LENGTH_SHORT).show();
            }
        }

    }

    fun updateGradeSuccessfully(){
        Toast.makeText(this, "The grade was sent !", Toast.LENGTH_SHORT).show();
    }

    fun updateGradeFailed(){
        Toast.makeText(this, "Error : The grade was not sent !", Toast.LENGTH_SHORT).show();
    }


}