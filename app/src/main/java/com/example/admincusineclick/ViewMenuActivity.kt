package com.example.admincusineclick

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincusineclick.adapter.MenuAdapter
import com.example.admincusineclick.databinding.ActivityViewMenuBinding
import com.example.admincusineclick.model.ItemDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewMenuActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var menuItem: ArrayList<ItemDetails> = ArrayList()


    private val binding: ActivityViewMenuBinding by lazy {
        ActivityViewMenuBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //database initializr
        databaseReference = FirebaseDatabase.getInstance().reference
        retriveMenuItems()

    }

    private fun retriveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        //fetch database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItem.clear()
                for (foodsnapshot in snapshot.children) {
                    val menuItems = foodsnapshot.getValue(ItemDetails::class.java)
                    menuItems?.let {
                        menuItem.add(it)
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
               val adapter = MenuAdapter(this, menuItem,databaseReference)
               binding.recyclerviewMenuItems.layoutManager = LinearLayoutManager(this)
               binding.recyclerviewMenuItems.adapter = adapter


    }
}