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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLoginButton = findViewById<Button>(R.id.goToLoginButton)
        goToLoginButton.setOnClickListener { goToLogin() }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener{ register() }

    }

    private fun goToLogin() {

        val loginActivity = Intent(this, LoginActivity::class.java).apply {

        }
        startActivity(loginActivity)
        finish()
    }

    private fun register(){

        val firstName = findViewById<EditText>(R.id.registerFirstNameInput)
        val lastName = findViewById<EditText>(R.id.registerLastNameInput)
        val email = findViewById<EditText>(R.id.registerEmailInput)
        val phone = findViewById<EditText>(R.id.registerPhoneInput)
        val address = findViewById<EditText>(R.id.registerAddressInput)
        val password = findViewById<EditText>(R.id.registerPasswordInput)
        val confirmPassword = findViewById<EditText>(R.id.registerConfirmPasswordInput)


        if(!firstName.text.isNullOrEmpty()){
            if(!lastName.text.isNullOrEmpty()){
                if(!email.text.isNullOrEmpty()){
                    if(!phone.text.isNullOrEmpty()){
                        if(!address.text.isNullOrEmpty()){
                            if(!password.text.isNullOrEmpty()){
                                if(!confirmPassword.text.isNullOrEmpty()){
                                    if(password.text.toString().equals(confirmPassword.text.toString())){

                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                                            .addOnCompleteListener(
                                                OnCompleteListener<AuthResult>{ task ->
                                                    if(task.isSuccessful){

                                                        val firebaseUser: FirebaseUser = task.result!!.user!!

                                                        val user = User(firstName.text.toString(),lastName.text.toString(),email.text.toString(),phone.text.toString(),address.text.toString(),firebaseUser.uid)

                                                        FirestoreController().addNewUser(this@RegisterActivity, user)

                                                        //FirebaseAuth.getInstance().signOut();


                                                    }else{
                                                        Toast.makeText(this, "Register failed : " + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            )


                                    }else{
                                        Toast.makeText(this, "Password not match !", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(this, "Password confirmation is missing !", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(this, "Password is missing !", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "Address is missing !", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Phone number is missing !", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Email is missing !", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Last Name is missing !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "First Name is missing !", Toast.LENGTH_SHORT).show();
        }

    }

    fun registeredSuccessfully(){
        Toast.makeText(this, "Registered successfully !", Toast.LENGTH_SHORT).show();
        goToLogin()
        finish()
    }

    fun registerFailed(){

    }

}