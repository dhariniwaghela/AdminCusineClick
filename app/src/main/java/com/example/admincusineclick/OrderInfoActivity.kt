package com.example.admincusineclick

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincusineclick.adapter.MenuAdapter
import com.example.admincusineclick.adapter.OrderAdapter
import com.example.admincusineclick.databinding.ActivityOrderInfoBinding
import com.example.admincusineclick.model.ItemDetails
import com.example.admincusineclick.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderInfoActivity : AppCompatActivity() {
    private val binding: ActivityOrderInfoBinding by lazy {
        ActivityOrderInfoBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var orderitem: ArrayList<Order> = ArrayList()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //database initializr
        databaseReference = FirebaseDatabase.getInstance().reference
        setAdapter()

        retriveOrders()

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun retriveOrders() {
        database = FirebaseDatabase.getInstance()
        val orderRef: DatabaseReference = database.reference.child("Order")

        //fetch database
        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderitem.clear()
                for (ordersnapshot in snapshot.children) {
                    val orderItems = ordersnapshot.getValue(Order::class.java)
                    orderItems?.let {
                        orderitem.add(it)
                    }
                }
                orderAdapter.updateList(orderitem)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error","error ${error.message}")
            }

        })
    }

    private fun setAdapter() {
        orderAdapter = OrderAdapter(this)
        binding.recyclerviewOrderitem.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewOrderitem.adapter = orderAdapter

    }
}