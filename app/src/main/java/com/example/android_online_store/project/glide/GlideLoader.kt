package com.example.android_online_store.project.glide

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.android_online_store.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageURI: Uri, imageView: ImageView){

        try{
            Glide.with(context)
                .load(imageURI)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }

    }


}