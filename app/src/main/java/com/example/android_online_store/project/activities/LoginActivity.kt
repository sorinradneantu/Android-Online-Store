package com.example.android_online_store.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android_online_store.R
import com.example.android_online_store.project.controllers.FirestoreController
import com.example.android_online_store.project.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val goToRegisterButton = findViewById<Button>(R.id.goToRegisterButton)
        goToRegisterButton.setOnClickListener { goToRegister() }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener { login() }

    }

    private fun goToRegister() {

        val registerActivity = Intent(this, RegisterActivity::class.java).apply {

        }
        startActivity(registerActivity)
        finish()
    }

    private fun login(){

        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)

        if(!email.text.isNullOrEmpty()){
            if(!password.text.isNullOrEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener{ task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Logged in !", Toast.LENGTH_SHORT).show();
                            FirestoreController().getAttributes(this@LoginActivity)
                        }else{
                            Toast.makeText(this, "Log in failed : "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

            }else{
                Toast.makeText(this, "Password is missing !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email is missing !", Toast.LENGTH_SHORT).show();
        }

    }

    fun loginSuccessfully(user: User){

        val mainActivity = Intent(this@LoginActivity, DashboardActivity::class.java).apply {

        }
        startActivity(mainActivity)
        finish()
    }

    fun loginFailed(){

    }


}