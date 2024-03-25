package com.example.admincusineclick

import android.R
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityAdminLoginBinding
import com.example.admincusineclick.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    val CUSTOM_PREF_NAME = "User_data"
    private lateinit var progressAlertDialog: AlertDialog
    private val binding: ActivityAdminLoginBinding by lazy {
        ActivityAdminLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressAlertDialog = createProgressDialog(this)

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
                displayProgressDialog()
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

    private fun createProgressDialog(currentActivity: AppCompatActivity): AlertDialog {
        val vLayout = LinearLayout(currentActivity)
        vLayout.orientation = LinearLayout.VERTICAL
        vLayout.setPadding(50, 50, 50, 50)
        vLayout.addView(ProgressBar(currentActivity, null, R.attr.progressBarStyleLarge))
        return AlertDialog.Builder(currentActivity)
            .setCancelable(false)
            .setView(vLayout)
            .create()
    }

    fun displayProgressDialog() {
        if (!progressAlertDialog.isShowing()) {
            progressAlertDialog.show()
        }
    }

    fun hideProgressDialog() {
        progressAlertDialog.dismiss()
    }

    val handler = Handler<String> {
        hideProgressDialog()
        when(it){
            "0" -> Toast.makeText(
                this@AdminLoginActivity,
                "Account may be  used by another user",
                Toast.LENGTH_SHORT
            ).show()
            "1" -> {
                val sharedPreferences = getSharedPreferences("AdminPref", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                myEdit.putString("userId", auth.currentUser?.uid)
                myEdit.apply()
                val loginintent = Intent(this@AdminLoginActivity, MainActivity::class.java)
                startActivity(loginintent)
            }
            "2" -> Toast.makeText(
                this@AdminLoginActivity,
                "Account doesn't exist or Invalid credentials",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun LoginUser() {
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
        })

        var isUserExist = -1
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                database = FirebaseDatabase.getInstance()
                val userDataReference = database.reference.child("Admin").child("AdminData");
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val userModel = ds.getValue(UserModel::class.java)!!
                            if(userModel.restaurantId != null && ds.key == auth.currentUser?.uid){
                                val tokenUpdateRef = userDataReference.child(auth.currentUser?.uid.toString())
                                val taskMap: MutableMap<String, Any> = HashMap()
                                taskMap["firebasetoken"] = token
                                tokenUpdateRef.updateChildren(taskMap)
                                isUserExist = 1
                                handler.call(isUserExist.toString())
                                break
                            }
                        }
                        if(isUserExist == -1){
                            isUserExist = 2
                            handler.call(isUserExist.toString())
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        isUserExist = 0
                        handler.call(isUserExist.toString())
                    }
                }
                userDataReference.addListenerForSingleValueEvent(valueEventListener)
            } else {
                isUserExist = 2
                handler.call(isUserExist.toString())

            }
        }
    }

    fun interface Handler<S> {
        fun call(String: S);
    }

    }


