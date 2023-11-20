package com.example.admincusineclick

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        binding.addMenuItem.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        })

        binding.viewMenu.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,ViewMenuActivity::class.java)
            startActivity(intent)
        })

        binding.orderInfo.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,OrderInfoActivity::class.java)
            startActivity(intent)
        })

        binding.addOfferBanner.setOnClickListener(View.OnClickListener {
            val i =Intent(this,AddOfferBannerActivity::class.java)
            startActivity(i)
        })

        binding.offerDetails.setOnClickListener(View.OnClickListener {
            val i =Intent(this,ViewOffersActivity::class.java)
            startActivity(i)
        })

        binding.profileInfo.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        })

        binding.logout.setOnClickListener(View.OnClickListener {

            auth!!.signOut()
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
            //logout but not kill fragment
            Toast.makeText(this, "Logout Successful !", Toast.LENGTH_SHORT).show()
        })

    }
}