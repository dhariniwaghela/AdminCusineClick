package com.example.admincusineclick

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityAdminLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {

        private lateinit var email: String
        private lateinit var password: String
        private lateinit var auth: FirebaseAuth
        private lateinit var database: FirebaseDatabase
    private val binding: ActivityAdminLoginBinding by lazy {
        ActivityAdminLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            //inialization of firebase auth
            auth = Firebase.auth

            //inialization of firebase database
            database = FirebaseDatabase.getInstance()


            //login with email and password
            binding.btnAdminLogin.setOnClickListener {
                //get data from fields
                email = binding.editTextAdminEmailAddress.text.toString().trim()
                password = binding.editTextAdminPassword.text.toString().trim()

                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
                } else {
                    createUser()
                }

            }
            binding.adminsignup.setOnClickListener {
                val signupintent = Intent(this, AdminSignupActivity::class.java)
                startActivity(signupintent)
            }
            binding.tvforgotpassword.setOnClickListener {
                val signupintent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(signupintent)
            }

        }

        private fun createUser() {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUi(user)
                    Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }


        }

        private fun updateUi(user: FirebaseUser?) {
            val mainintent = Intent(this, MainActivity::class.java)
            startActivity(mainintent)
            finish()

        }
    }
