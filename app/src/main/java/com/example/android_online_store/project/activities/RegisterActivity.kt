package com.example.android_online_store.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.android_online_store.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginButton = findViewById<Button>(R.id.goToLoginButton)
        goToLoginButton.setOnClickListener { goToLogin() }

    }

    private fun goToLogin() {

        val loginActivity = Intent(this, LoginActivity::class.java).apply {

        }
        startActivity(loginActivity)
        finish()
    }

}