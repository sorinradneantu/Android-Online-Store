package com.example.android_online_store.project.controllers

import android.util.Log
import com.example.android_online_store.project.activities.RegisterActivity
import com.example.android_online_store.project.models.User
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
}