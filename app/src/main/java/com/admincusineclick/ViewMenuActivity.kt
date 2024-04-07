package com.admincusineclick

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.admincusineclick.adapter.MenuAdapter
import com.admincusineclick.databinding.ActivityViewMenuBinding
import com.admincusineclick.model.ItemDetails
import com.admincusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewMenuActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var menuItem: ArrayList<ItemDetails> = ArrayList()
    private lateinit var menuAdapter: MenuAdapter


    private lateinit var auth: FirebaseAuth
    private var restaurantId : String = null.toString()


    private val binding: ActivityViewMenuBinding by lazy {
        ActivityViewMenuBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //database initializr
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Log.d("user", user.toString())

        if (user != null) {
            restaurantId = user.uid
            databaseReference = FirebaseDatabase.getInstance().reference
            setAdapter()
            retriveMenuItems()

        }

        binding.buttonBack.setOnClickListener {
            finish()
        }


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
                    if (menuItems!!.restaurantId == restaurantId) {
                        menuItems?.let {
                            menuItem.add(it)
                        }
                    }
                }
                menuAdapter.updateList(menuItem)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", "error ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        menuAdapter = MenuAdapter(this)
        binding.recyclerviewMenuItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewMenuItems.adapter = menuAdapter

    }
}