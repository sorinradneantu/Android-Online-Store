package com.example.android_online_store.project.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.activities.SoldProductDetailsActivity
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.SoldProduct

open class SoldProductsAdapter(
    private val context: Context,
    private var list: ArrayList<SoldProduct>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SoldProductsAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.product_list_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            GlideLoader(context).loadProdImage(model.image, holder.itemView.findViewById(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = model.price

            holder.itemView.findViewById<TextView>(R.id.tv_item_description).visibility = View.GONE
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context,SoldProductDetailsActivity::class.java)
                intent.putExtra("SoldItems",model)
                context.startActivity(intent)
            }

        }

    }


    override fun getItemCount():Int{
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


}