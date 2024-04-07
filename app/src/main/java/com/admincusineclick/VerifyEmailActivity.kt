package com.admincusineclick

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.admincusineclick.databinding.ActivityVerifyEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VerifyEmailActivity : AppCompatActivity() {

    private val binding: ActivityVerifyEmailBinding by lazy {
        ActivityVerifyEmailBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        user= auth.currentUser!!

        binding.btnVerify.setOnClickListener(View.OnClickListener {
            user!!.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // email sent
                        // after email is sent just logout the user and finish this activity
                        val sharedPreferences = getSharedPreferences("AdminPref", MODE_PRIVATE)
                        val myEdit = sharedPreferences.edit()
                        myEdit.putString("userId", user.uid)
                        myEdit.apply()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // email not sent, so display message and restart the activity or do whatever you wish to do

                        //restart this activity
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                    }
                }
        })
    }
}