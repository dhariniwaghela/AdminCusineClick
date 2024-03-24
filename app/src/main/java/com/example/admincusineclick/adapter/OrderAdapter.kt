package com.example.admincusineclick.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admincusineclick.databinding.OrderInfoSingleItemBinding
import com.example.admincusineclick.model.Order
import com.example.admincusineclick.notificationApi.FcmApi
import com.example.admincusineclick.notificationApi.FcmNotification
import com.example.admincusineclick.notificationApi.FcmResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderAdapter(private val context: Context)  :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var OrderInfo: ArrayList<Order> = ArrayList()
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    //firebase instance
    private var menuDatabaseReference: DatabaseReference

    fun updateList(orderItem: MutableList<Order>) {
        OrderInfo = orderItem as ArrayList<Order>
        notifyDataSetChanged()
    }


    init {
        val database = FirebaseDatabase.getInstance()
        menuDatabaseReference = database.reference.child("Order")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.OrderViewHolder {
        val binding = OrderInfoSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int= OrderInfo.size



    inner class OrderViewHolder(private val binding: OrderInfoSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val userId = OrderInfo[position].userId.toString()
                //fetching user info
            binding.apply {
                orderFoodPrice.text = OrderInfo[position].OrderAmount.toString()
                userName.text = OrderInfo[position].userId.toString()
                userLocation.text = OrderInfo[position].userLocation.toString()
                userName.text = OrderInfo[position].userName.toString()

            }

            binding.btnAcceptOrder.setOnClickListener {

                var token = ""
                if (userId.isNotEmpty()) {
                    //fetching user info
                    databaseReference.child(userId)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userObj = snapshot.value as Map<String, Object>
                                token = userObj["firebasetoken"].toString()

                                val retrofit = Retrofit.Builder()
                                    .baseUrl("https://fcm.googleapis.com/fcm/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()

                                val fcmApi = retrofit.create(FcmApi::class.java)

                                val notification = FcmNotification(
                                    to = token,
                                    data = mapOf(
                                        "title" to "Your push notification message"
                                    )
                                )

                                val call = fcmApi.sendNotification(notification)
                                call.enqueue(object : Callback<FcmResponse> {
                                    override fun onResponse(call: Call<FcmResponse>, response: Response<FcmResponse>) {
                                        if (response.isSuccessful) {
                                            val fcmResponse = response.body()
                                            // Handle successful response
                                            Toast.makeText(context,"Task",Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Handle error response
                                        }
                                    }

                                    override fun onFailure(call: Call<FcmResponse>, t: Throwable) {
                                        // Handle failure
                                    }
                                })

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }
        }


        // private fun sendPushNotification(token: String) {
       //     val url = "https://fcm.googleapis.com/fcm/send" }
    }
}








