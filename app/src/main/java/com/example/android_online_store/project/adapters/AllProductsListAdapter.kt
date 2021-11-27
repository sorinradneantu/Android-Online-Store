package com.example.android_online_store.project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Product

open class AllProductsListAdapter(val context: Context, var prodlist: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_products_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position:Int){
        val model = prodlist[position]

        if(holder is MyViewHolder){

            GlideLoader(context).loadProdImage(model.image,holder.itemView.findViewById<ImageView>(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "${model.price} $"
            holder.itemView.findViewById<TextView>(R.id.tv_item_description).text = model.description
        }

    }

    override fun getItemCount():Int{
        return prodlist.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}