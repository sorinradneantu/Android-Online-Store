package com.example.android_online_store.project.controllers

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.example.android_online_store.project.activities.*
import com.example.android_online_store.project.activities.ui.dashboard.DashboardFragment
import com.example.android_online_store.project.activities.ui.products.ProductsFragment
import com.example.android_online_store.project.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreController {

    private val db = FirebaseFirestore.getInstance()


    fun addNewUser(activity: RegisterActivity, user: User){

        db.collection("users")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.registeredSuccessfully()
            }
            .addOnFailureListener { e->
                activity.registerFailed()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error register !",
                    e
                )
            }

    }

    fun getId():String{

        var userId = ""
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            userId = user.uid
        }
        return userId
    }

    fun getAttributes(activity: Activity){
        db.collection("users")
            .document(getId())
            .get()
            .addOnSuccessListener { document ->

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences("shopPreferences",MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    "firstname_logged",
                    "${user.firstName}"
                )
                editor.putString(
                    "lastname_logged",
                    "${user.lastName}"
                )
                editor.putString(
                    "email_logged",
                    "${user.email}"
                )
                editor.putString(
                    "address_logged",
                    "${user.address}"
                )
                editor.putString(
                    "phoneNr_logged",
                    "${user.phoneNumber}"
                )
                editor.putString(
                    "username_logged",
                    "${user.firstName} ${user.lastName}"
                )
                editor.putString(
                    "grade",
                    "${user.grade}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity -> {
                        activity.loginSuccessfully(user)
                    }
                }
            }
            .addOnFailureListener { e->

                when(activity){
                    is LoginActivity ->{
                        activity.loginFailed()
                    }
                }

            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {

                            is NewProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is NewProductActivity -> {
                        activity.imageUploadFailed()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }



    fun addProduct(activity: NewProductActivity,product: Product){

        db.collection("products")
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener {
                activity.productUploadFail()
            }

    }


    fun getProductsList(fragment: Fragment){
        db.collection("products")
            .whereEqualTo("owner_id",getId())
            .get()
            .addOnSuccessListener { document ->

                val prodList: ArrayList<Product> = ArrayList()

                for(index in document.documents){
                    val prod = index.toObject(Product::class.java)
                    prod!!.prod_id = index.id

                    prodList.add(prod)
                }

                when(fragment){
                    is ProductsFragment ->
                        fragment.successGetProdFromDB(prodList)
                }

            }
    }

    fun getAllProductsList(fragment: DashboardFragment){
        db.collection("products")
            .whereNotEqualTo("owner_id",getId())
            .get()
            .addOnSuccessListener { document ->

                val allProdList: ArrayList<Product> = ArrayList()

                for(index in document.documents){
                    val prod = index.toObject(Product::class.java)!!
                    prod.prod_id = index.id
                    allProdList.add(prod)
                }

                fragment.successGetAllProductsList(allProdList)

            }

    }

    fun deleteProd(fragment: ProductsFragment, prodId: String){
        db.collection("products")
            .document(prodId)
            .delete()
            .addOnSuccessListener {
                fragment.prodDeleteSuccess()
            }
    }

    fun getProductDetails(activity: ProductWindowActivity, productId: String){
        db.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { document ->

                val product = document.toObject(Product::class.java)

                if (product != null) {
                    activity.getDetailsSuccess(product)
                }

            }.addOnFailureListener {
                e -> Log.e(activity.javaClass.simpleName, "error :",e)
            }
    }


fun addProductToCart(activity: ProductWindowActivity, cart_product: Cart_Product){
    db.collection("cart_items")
        .document()
        .set(cart_product, SetOptions.merge())
        .addOnSuccessListener {
            activity.productAddedToCartSuccessfully()
        }.addOnFailureListener {
            activity.productAddedToCartFailed()
        }

    }

    fun checkProductExistInCart(activity: ProductWindowActivity, prodId:String){
        db.collection("cart_items")
            .whereEqualTo("user_id",getId())
            .whereEqualTo("product_id",prodId)
            .get()
            .addOnSuccessListener { document ->
                if(document.documents.size != 0){
                    activity.prodExistAlreadyInCart()
                }
            }.addOnFailureListener{ e ->
                Log.e(activity.javaClass.simpleName,"error",e)
            }
    }

    fun getCartProducts(activity: Activity){
        db.collection("cart_items")
            .whereEqualTo("user_id",getId())
            .get()
            .addOnSuccessListener { document ->

                val prodList: ArrayList<Cart_Product> = ArrayList()

                for(index in document.documents){
                    val prod = index.toObject(Cart_Product::class.java)!!
                    prod.id = index.id
                    prodList.add(prod)
                }

                when(activity){
                    is CartActivity -> {
                        activity.getCartProductsSuccessfully(prodList)
                    }
                    is CheckoutActivity -> {
                        activity.successGetCartItemsList(prodList)
                    }
                }

            }.addOnFailureListener {
                when(activity){
                    is CartActivity -> {
                        activity.getCartProductsFailed()
                    }
                    is CheckoutActivity -> {
                        activity.failedGetCartItemsList()
                    }
                }
            }
    }

    fun getAllProductsList(activity: Activity){

        db.collection("products")
            .get()
            .addOnSuccessListener { document ->

                val prodList: ArrayList<Product> = ArrayList()

                for(index in document.documents){

                    val prod = index.toObject(Product::class.java)
                    prod!!.prod_id = index.id
                    prodList.add(prod)

                }

                when(activity){
                    is CartActivity -> {
                        activity.getAllProductsFromDBSuccessfully(prodList)
                    }
                    is CheckoutActivity -> {
                        activity.succestGetProductList(prodList)
                    }
                }


            }.addOnFailureListener {

            }

    }

    fun removeProductFromCart(context: Context, id_cart: String){

        db.collection("cart_items")
            .document(id_cart)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartActivity -> {
                        context.productRemovedFromCartSuccessfully()
                    }
                }
            }
            .addOnFailureListener { e ->
                when(context){
                    is CartActivity -> {
                        context.productRemovedFromCartFailed()
                    }
                }

            }

    }

    fun updateCart(context: Context, id_cart: String, itemHashMap: HashMap<String, Any>){

        db.collection("cart_items")
            .document(id_cart)
            .update(itemHashMap)
            .addOnSuccessListener {

                when(context){
                    is CartActivity -> {
                        context.updateCartSuccessfully()
                    }
                }

            }
            .addOnFailureListener {
                when(context){
                    is CartActivity -> {
                        context.updateCartFailed()
                    }
                }
            }

    }


    fun placeOrder(activity: CheckoutActivity, order: Order){

        db.collection("orders")
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {

                activity.placeOrderSuccessfully()

            }
            .addOnFailureListener {

                activity.placeOrderFailed()

            }

    }

    fun update(activity: CheckoutActivity, cartList: ArrayList<Cart_Product>, order: Order){

        val outBatch = db.batch();

        for(cartProduct in cartList){

         val soldProduct = SoldProduct(
             cartProduct.product_owner_id,
             cartProduct.product_name,
             cartProduct.price,
             cartProduct.cart_quantity,
             cartProduct.image,
             order.details,
             order.date,
             order.sub_total_amount,
             order.shipping_charge,
             order.total_amount,
             order.address
         )

            val ref = db.collection("sold_products")
                .document(cartProduct.product_id)

            outBatch.set(ref,soldProduct)

        }

        for(cartProduct in cartList){

            val product = HashMap<String,Any>()

            product["quantity"] = (cartProduct.prod_quantity.toInt() - cartProduct.cart_quantity.toInt()).toString()

            val ref = db.collection("products")
                .document(cartProduct.product_id)

            outBatch.update(ref,product)

        }

        for (cartProduct in cartList) {

            val ref = db.collection("cart_items")
                .document(cartProduct.id)
            outBatch.delete(ref)
        }

        outBatch.commit().addOnSuccessListener {
            activity.updateSuccessfully()
        }.addOnFailureListener {
            activity.updateFailed()
        }

    }

    fun getOrders(activity: MyOrdersActivity){
        db.collection("orders")
            .whereEqualTo("user_id",getId())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()

                for(index in document.documents){

                    val orderItem = index.toObject(Order::class.java)!!
                    orderItem.id = index.id

                    list.add(orderItem)

                }

            activity.getOrdersInUI(list)

            }.addOnFailureListener {
            activity.getOrdersFail()
            }
    }

    fun getSoldProductsList(activity: SoldProductActivity) {
        db.collection("sold_products")
            .whereEqualTo("user_id", getId())
            .get()
            .addOnSuccessListener { document ->

                val list: ArrayList<SoldProduct> = ArrayList()

                for (index in document.documents) {

                    val soldProduct = index.toObject(SoldProduct::class.java)!!
                    soldProduct.id = index.id

                    list.add(soldProduct)
                }

                activity.successSoldProductsList(list)
            }
            .addOnFailureListener {

                activity.failSoldProductsList()
            }

    }

    fun updateGrade(activity: ProductWindowActivity,userID: String,itemHashMap: HashMap<String, Any>){

        db.collection("users")
            .document(userID)
            .update(itemHashMap).addOnSuccessListener {

                        activity.updateGradeSuccessfully()

            }.addOnFailureListener {

                      activity.updateGradeFailed()


            }


    }

    /*
    fun getOwnerGrade(activity: ProductWindowActivity,ownerID: String){

        db.collection("users")
            .document(ownerID)
            .get()
            .addOnSuccessListener {
                    document ->

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences("OwnerGrade",MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    "grade",
                    "${user.grade}"
                )
                editor.apply()
            }.addOnFailureListener {

            }

    }
*/

}