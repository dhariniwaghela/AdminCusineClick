package com.example.admincusineclick

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityMainBinding
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        binding.profileInfo.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener(View.OnClickListener {

            auth!!.signOut()
            val preferenceManager = getSharedPreferences("AdminPref", Context.MODE_PRIVATE)
            preferenceManager?.edit()?.clear()?.apply()
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
            finish()
            //logout but not kill fragment
            Toast.makeText(this, "Logout Successful !", Toast.LENGTH_SHORT).show()
        })

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    var hasNotificationPermissionGranted = false
}