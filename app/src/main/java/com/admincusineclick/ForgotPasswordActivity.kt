package com.admincusineclick

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admincusineclick.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private val binding: ActivityForgotPasswordBinding by lazy{
        ActivityForgotPasswordBinding.inflate(layoutInflater )

    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnReset.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                Toast.makeText(this,"Link Has been send to Reset the Password",Toast.LENGTH_LONG).show()
                finish()
                val mainintent= Intent(this, AdminLoginActivity::class.java)
                startActivity(mainintent)

            }
                .addOnFailureListener{

                }

        }    }
}