package com.example.android_online_store.project.controllers

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.android_online_store.project.activities.LoginActivity
import com.example.android_online_store.project.activities.NewProductActivity
import com.example.android_online_store.project.activities.RegisterActivity
import com.example.android_online_store.project.models.Product
import com.example.android_online_store.project.models.User
import com.google.android.gms.common.internal.Constants
import com.google.common.io.Files.getFileExtension
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



}