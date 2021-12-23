package com.example.android_online_store.project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.activities.CartActivity
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Cart_Product

open class CartListAdapter(private val context: Context, private var prodList: ArrayList<Cart_Product>, private val updateCartItems: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.cart_product_layout, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = prodList[position]

        if(holder is MyViewHolder){

            GlideLoader(context).loadProdImage(model.image, holder.itemView.findViewById(R.id.iv_image))

            holder.itemView.findViewById<TextView>(R.id.tv_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.tv_price).text = "${model.price} $"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = model.cart_quantity

            if(model.cart_quantity == "0"){
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove).visibility = View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add).visibility = View.GONE
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = "Not available"


                if(updateCartItems){
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete).visibility = View.VISIBLE
                }else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete).visibility = View.GONE
                }

            }else{
                if(updateCartItems){
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete).visibility = View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add).visibility = View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove).visibility = View.VISIBLE
                }else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove).visibility = View.GONE
                }

            }

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete).setOnClickListener {
                FirestoreController().removeProductFromCart(context, model.id)
            }

            holder.itemView.findViewById<ImageButton>(R.id.ib_remove).setOnClickListener {
                if(model.cart_quantity == "1"){
                    FirestoreController().removeProductFromCart(context, model.id)
                }else{

                    val cartQ: Int = model.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap["cart_quantity"] = (cartQ - 1).toString()

                    FirestoreController().updateCart(context,model.id,itemHashMap)
                }
            }

            holder.itemView.findViewById<ImageButton>(R.id.ib_add).setOnClickListener {

                val cartQ: Int = model.cart_quantity.toInt()
                if(cartQ < model.prod_quantity.toInt()){
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap["cart_quantity"] = (cartQ + 1).toString()

                    FirestoreController().updateCart(context,model.id,itemHashMap)
                }else{
                    when(context){
                        is CartActivity -> {
                            context.showQuantityError()
                        }
                    }
                }

            }


        }

    }

    override fun getItemCount(): Int {
        return prodList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}