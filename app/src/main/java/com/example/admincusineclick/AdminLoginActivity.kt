package com.example.admincusineclick

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityAdminLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                 LoginUser()
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

    private fun LoginUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var isAdminFound= false
                val admindatareference = database.reference.child("Admin").child("AdminData")
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val adminEmail = ds.child("email").getValue(String::class.java)
                            val adminPassword = ds.child("password").getValue(String::class.java)
                            isAdminFound = adminEmail == email && adminPassword == password
                        }
                        if (isAdminFound) {
                            val mainintent = Intent(this@AdminLoginActivity, MainActivity::class.java)
                            startActivity(mainintent)
                            finish()
                            Toast.makeText(this@AdminLoginActivity, "Login Successfull", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@AdminLoginActivity, "Account Does not exist ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(TAG, databaseError.getMessage()) //Don't ignore errors!
                        Toast.makeText(this@AdminLoginActivity, "Account Does not exist ", Toast.LENGTH_SHORT).show()
                    }
                }
                admindatareference.addListenerForSingleValueEvent(valueEventListener)

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
