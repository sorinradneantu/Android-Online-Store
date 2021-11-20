package com.example.android_online_store.project.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android_online_store.R
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.glide.GlideLoader
import com.example.android_online_store.project.models.Product
import com.example.android_online_store.project.others.RequestCodes
import com.example.android_online_store.project.others.RequestCodes.PICK_IMAGE_REQUEST_CODE
import java.io.IOException

class NewProductActivity : AppCompatActivity(), View.OnClickListener {

    private var mSelectedImageFileUri: Uri? = null

    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)

        val add_product = findViewById<ImageView>(R.id.iv_add_update_product)
        add_product.setOnClickListener(this)

        val finish_button = findViewById<Button>(R.id.finish_button)
        finish_button.setOnClickListener { uploadProcess() }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        showImageChooser(this@NewProductActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            RequestCodes.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

            }
        }
    }

    private fun showImageChooser(activity: Activity){

        val gallery = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {

        }

        activity.startActivityForResult(gallery,PICK_IMAGE_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == RequestCodes.READ_STORAGE_PERMISSION_CODE){

            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser(this)
            }else{
                Toast.makeText(this, "Permission denied !", Toast.LENGTH_SHORT).show();
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RequestCodes.PICK_IMAGE_REQUEST_CODE){
                if(data != null){
                    val add_product = findViewById<ImageView>(R.id.iv_add_update_product)
                    val product_image = findViewById<ImageView>(R.id.iv_product_image)
                    add_product.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))

                     mSelectedImageFileUri = data.data!!

                    try{
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, product_image)
                    }catch(e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Image selection canceled !", Toast.LENGTH_SHORT).show();
        }
    }

    fun uploadProcess(){
        if(validate() == 1){
            uploadProductImage()
        }
    }

    fun imageUploadSuccess(imageURL: String){
        //Toast.makeText(this, "Image uploaded successfully !", Toast.LENGTH_SHORT).show();
        mProductImageURL = imageURL

        addProductToDatabase()
    }

    fun imageUploadFailed(){
        Toast.makeText(this, "Image upload failed !", Toast.LENGTH_SHORT).show();
    }

    private fun uploadProductImage(){

        FirestoreController().uploadImageToCloudStorage(this, mSelectedImageFileUri,"Product_Image");

    }

    private fun validate():Int{

        val productNameBox = findViewById<EditText>(R.id.productNameInput)
        val priceBox = findViewById<EditText>(R.id.priceInput)
        val descriptionBox = findViewById<EditText>(R.id.descriptionInput)
        val quantityBox = findViewById<EditText>(R.id.quantityInput)


        if(!productNameBox.text.isNullOrEmpty()){
            if(!priceBox.text.isNullOrEmpty()){
                if(!descriptionBox.text.isNullOrEmpty()){
                    if(!quantityBox.text.isNullOrEmpty()){
                        if(mSelectedImageFileUri != null){
                            return 1
                        }else{
                            Toast.makeText(this, "Image is missing !", Toast.LENGTH_SHORT).show();
                            return 0
                        }
                    }else{
                        Toast.makeText(this, "Quantity is missing !", Toast.LENGTH_SHORT).show();
                        return 0
                    }
                }else{
                    Toast.makeText(this, "Description is missing !", Toast.LENGTH_SHORT).show();
                    return 0
                }
            }else{
                Toast.makeText(this, "Price is missing !", Toast.LENGTH_SHORT).show();
                return 0
            }
        }else{
            Toast.makeText(this, "Product name is missing !", Toast.LENGTH_SHORT).show()
            return 0
        }
    }

    private fun addProductToDatabase(){

        val productNameBox = findViewById<EditText>(R.id.productNameInput)
        val priceBox = findViewById<EditText>(R.id.priceInput)
        val descriptionBox = findViewById<EditText>(R.id.descriptionInput)
        val quantityBox = findViewById<EditText>(R.id.quantityInput)

        val username = this.getSharedPreferences("shopPreferences", MODE_PRIVATE).getString("username_logged","")


        val user_id = FirestoreController().getId()

        val product = Product("",productNameBox.text.toString(),priceBox.text.toString(),descriptionBox.text.toString(),quantityBox.text.toString(),mProductImageURL,user_id,
            username!!
        )


        FirestoreController().addProduct(this@NewProductActivity,product)

    }

    fun productUploadSuccess(){
        Toast.makeText(this, "Product uploaded successfully !", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun productUploadFail(){
        Toast.makeText(this, "Product upload failed !", Toast.LENGTH_SHORT).show()
    }
}