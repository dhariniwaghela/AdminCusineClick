package com.example.admincusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var mAuth: FirebaseAuth? = null

            mAuth = FirebaseAuth.getInstance()

            Handler().postDelayed({
                if (mAuth!!.getCurrentUser() != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this,AdminLoginActivity ::class.java))
                }
                finish()
            }, 3000)
    }
}