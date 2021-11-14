package com.example.android_online_store.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.android_online_store.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val goToRegisterButton = findViewById<Button>(R.id.goToRegisterButton)
        goToRegisterButton.setOnClickListener { goToRegister() }

    }

    private fun goToRegister() {

        val registerActivity = Intent(this, RegisterActivity::class.java).apply {

        }
        startActivity(registerActivity)
        finish()
    }

}