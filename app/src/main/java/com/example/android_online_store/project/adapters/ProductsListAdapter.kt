package com.example.android_online_store.project.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_online_store.R
import com.example.android_online_store.project.activities.ProductWindowActivity
import com.example.android_online_store.project.activities.ui.products.ProductsFragment
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Product



open class ProductsListAdapter(val context: Context, var prodlist: ArrayList<Product>,val fragment: ProductsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.product_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position:Int){
        val model = prodlist[position]

        if(holder is MyViewHolder){

            GlideLoader(context).loadProdImage(model.image,holder.itemView.findViewById<ImageView>(R.id.iv_item_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.product_name
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "${model.price} $"
            holder.itemView.findViewById<TextView>(R.id.tv_item_description).text = model.description

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener{
                fragment.deleteProduct(model.prod_id)
            }

            holder.itemView.setOnClickListener {
                val productWindow = Intent(context, ProductWindowActivity::class.java)
                productWindow.putExtra("ProductID",model.prod_id)
                context.startActivity(productWindow)
            }
        }

    }

    override fun getItemCount():Int{
        return prodlist.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}