package com.example.admincusineclick

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityAdminSignupBinding
import com.example.admincusineclick.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.regex.Pattern


class AdminSignupActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var restaurantName: String
    private lateinit var location: String


    private val binding: ActivityAdminSignupBinding by lazy {
        ActivityAdminSignupBinding.inflate(layoutInflater)
    }
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private fun isValidString(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        FirebaseApp.initializeApp(this);
        binding.adminloginscreen.setOnClickListener {
            val loginintent = Intent(this, AdminLoginActivity::class.java)
            startActivity(loginintent)
        }

        val locationList =
            arrayOf(
                "Toronto",
                "Hamilton",
                "Ottawa",
                "Mississauga",
                "London",
                "Kitchener",
                "Markham",
                "Windsor",
                "Oshawa",
                "Barrie"
            )
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            location = parent.getItemAtPosition(position) as String
        }
        //initialize firebase database
        auth = Firebase.auth
        //initialize database
        database = Firebase.database.reference

        binding.btnAdminSignup.setOnClickListener {

            username = binding.editTextAdminName.text.toString()
            email = binding.edittextEmail.text.toString().trim()
            password = binding.edittextPassword.text.toString().trim()
            restaurantName = binding.editTextRestaurantName.text.toString().trim()


            if (email.isEmpty() || password.isBlank() || username.isBlank() || restaurantName.isEmpty()) {
                binding.editTextAdminName.error = "name should not be empty"
                binding.edittextEmail.error = "email should not be empty"
                binding.edittextPassword.error = "Password should not be empty"
                binding.editTextRestaurantName.error = "Restaurant name should not be empty"
            } else if (!isValidString(email)) {
                binding.edittextEmail.error = "Invalid email format"
            } else if (password.length < 6) {
                binding.edittextPassword.error = "Password must have atleast 6 caracters"
            } else {
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                sendVerificationEmail()
                finish()
            } else {
                Toast.makeText(this, "Email is already existed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "create Account:Failure", task.exception)
            }
        }

    }

    private fun saveUserData() {
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

        token = task.result
        //retrive data
        username = binding.editTextAdminName.text.toString()
        email = binding.edittextEmail.text.toString().trim()
        password = binding.edittextPassword.text.toString().trim()
        restaurantName = binding.editTextRestaurantName.text.toString().trim()


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val user = UserModel(userId, username, email, password, restaurantName,token)
        //save user data
        database.child("Admin").child("AdminData").child(userId).setValue(user)
        })

    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    Toast.makeText(this, "Verification link has been send", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, VerifyEmailActivity::class.java)

                    startActivity(intent)
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
    }
}

