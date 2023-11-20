package com.example.admincusineclick

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincusineclick.adapter.OfferAdapter
import com.example.admincusineclick.databinding.ActivityViewOffersBinding
import com.example.admincusineclick.model.BannerDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewOffersActivity : AppCompatActivity() {
    private val binding: ActivityViewOffersBinding by lazy {
        ActivityViewOffersBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var bannerItem: ArrayList<BannerDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //database initializr
        databaseReference = FirebaseDatabase.getInstance().reference
        if(bannerItem.size > 0) {
            retrieveBanners()
        }else{
            Toast.makeText(this,"no data show",Toast.LENGTH_SHORT).show()
        }

    }
    private fun retrieveBanners(){
        database = FirebaseDatabase.getInstance()
        val bannerRef: DatabaseReference = database.reference.child("Banner")

        //fetch database
        bannerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (bannersnapshot in snapshot.children) {
                    val bannerItems = bannersnapshot.getValue(BannerDetails::class.java)
                    bannerItems?.let {
                        bannerItem.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error","error ${error.message}")
            }

        })
    }
    private fun setAdapter() {
        val adapter = OfferAdapter(this, bannerItem,databaseReference)
        binding.recyclerviewBanner.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewBanner.adapter = adapter

    }

}