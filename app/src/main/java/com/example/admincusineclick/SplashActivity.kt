package com.example.admincusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val options = FirebaseOptions.Builder()
            .setProjectId("admincusineclick")
            .setApplicationId("1:585183072161:android:8f331373e89a62492e05e0")
            .setApiKey("AIzaSyBSvmguvL2E5UIt3Dwe1rZ87im4hHh7zyo") // setDatabaseURL(...)
            // setStorageBucket(...)
            .build()

        val app1 = FirebaseApp.initializeApp(this /* Context */, options,"AdminApp")
//        val auth1 = FirebaseAuth.getInstance(app1)

        var mAuth: FirebaseAuth? = null

            mAuth = FirebaseAuth.getInstance()

            Handler().postDelayed({
                val sh = getSharedPreferences("AdminPref", MODE_PRIVATE)
                val userId = sh.getString("userId","")
                if (userId != null && userId.isNotEmpty()) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this,AdminLoginActivity ::class.java))
                }
                finish()
            }, 3000)
    }
}