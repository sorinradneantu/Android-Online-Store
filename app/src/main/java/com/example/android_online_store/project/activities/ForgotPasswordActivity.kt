package com.example.android_online_store.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.android_online_store.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        val sendButton = findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            send()
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            backToLogin()
        }

    }

    private fun send(){

        val email: String = findViewById<TextView>(R.id.fwPwEmail).text.toString()

        if(email.isNullOrEmpty()){
            Toast.makeText(this, "Email is empty !", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "An email was sent to "+email+" !", Toast.LENGTH_SHORT).show();
                    backToLogin()
                }else{
                    Toast.makeText(this, "Sending email failed : "+task.exception!!.message.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun backToLogin(){
        val backToLogin = Intent(this, LoginActivity::class.java).apply {

        }
        startActivity(backToLogin)
        finish()
    }
}