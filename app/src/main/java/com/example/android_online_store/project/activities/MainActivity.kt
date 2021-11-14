package com.example.android_online_store.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.android_online_store.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("shopPreferences", MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname_logged", "")

        val test = findViewById<TextView>(R.id.textview)
        test.text = "Hello, ${firstname}"
    }
}