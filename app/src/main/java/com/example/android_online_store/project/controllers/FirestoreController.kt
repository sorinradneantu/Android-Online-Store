package com.example.android_online_store.project.controllers

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.android_online_store.project.activities.LoginActivity
import com.example.android_online_store.project.activities.RegisterActivity
import com.example.android_online_store.project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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


}