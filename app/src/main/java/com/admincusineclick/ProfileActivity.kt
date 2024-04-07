package com.admincusineclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.admincusineclick.databinding.ActivityProfileBinding
import com.admincusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Log.d("user",user.toString())

        if (user != null) {
            val userId = user.uid
            databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child("AdminData").child(userId)
           Log.d("uid",userId)
            getAdminData()

        }

        binding.btnedit.setOnClickListener{
            val intent = Intent(this,EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.btnforgot.setOnClickListener{
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    private fun getAdminData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(UserModel::class.java)!!
                binding.tvName.setText(userinfo.name)
                binding.tvEmail.setText(userinfo.email)
                binding.tvRestaturant.setText(userinfo.restaurantName)
                val imageUrl = userinfo.imgUri
                this.let { Glide.with(this@ProfileActivity).load(imageUrl).placeholder(R.drawable.profile).error(R.drawable.profile).into(binding.profileImage) }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}