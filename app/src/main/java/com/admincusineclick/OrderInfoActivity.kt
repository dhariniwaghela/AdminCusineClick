package com.admincusineclick

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.admincusineclick.adapter.OrderAdapter
import com.admincusineclick.databinding.ActivityOrderInfoBinding
import com.admincusineclick.model.Order
import com.admincusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects


class OrderInfoActivity : AppCompatActivity() {
    private val binding: ActivityOrderInfoBinding by lazy {
        ActivityOrderInfoBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    var orderitem : MutableList<Order> = arrayListOf()
    private lateinit var orderAdapter: OrderAdapter

    //get restaurant name to fetch order details
    private lateinit var restaurantinfo : UserModel
    private lateinit var auth: FirebaseAuth
    private lateinit var restaurantDatabaseReference: DatabaseReference
    private lateinit var UserDatabaseReference: DatabaseReference
    var restaurantname : String? = ""
    var userid : String? = ""
    var userlocation = ""
    var username = ""
    //order item info saveing var


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //database initializr
        databaseReference = FirebaseDatabase.getInstance().reference

        getRestaurantName()

        setAdapter()

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun getRestaurantName() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            restaurantDatabaseReference = FirebaseDatabase.getInstance().getReference("Admin").child("AdminData").child(userId)
            restaurantDatabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    restaurantinfo = snapshot.getValue(UserModel::class.java)!!
                     restaurantname = restaurantinfo.restaurantName
                    if(restaurantname!= null && restaurantname!!.isNotEmpty()){
                        retriveOrders()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


    private fun retriveOrders() {
        database = FirebaseDatabase.getInstance()
        val orderRef: DatabaseReference = database.reference.child("Order")

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderitem.clear()
                for (ordersnapshot in snapshot.children) {
                    userid = ordersnapshot.key
                    for (snapShotTimeStamp in ordersnapshot.children) {
                        for (restaurntSnapShot in snapShotTimeStamp.children) {
                            if (restaurntSnapShot.key == restaurantname) {
                                val orderObj = restaurntSnapShot.value as Map<String, Object>
                                orderitem.add(
                                    Order(
                                        userId = userid,
                                        OrderAmount = orderObj["OrderAmount"]?.toString(),
                                        userName = orderObj["Username"].toString(),
                                        userLocation = orderObj["UserLocation"].toString()
                                    )
                                )
                            }
                        }
                    }
                }
                orderAdapter.updateList(orderitem)
                val sharedPreferences = getSharedPreferences("AdminPref", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                myEdit.putString("ordercount",orderitem.size.toString())
                myEdit.apply()
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