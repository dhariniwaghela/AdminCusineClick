package com.admincusineclick.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.math.BigDecimal
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.admincusineclick.Firebase.FcmApi
import com.admincusineclick.Firebase.RequestNotification
import com.admincusineclick.Firebase.SendNotificationModel
import com.admincusineclick.databinding.OrderInfoSingleItemBinding
import com.admincusineclick.getClient
import com.admincusineclick.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat


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
        databaseReference = database.reference.child("User").child("UserData")
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
                orderFoodPrice.text ="OrderAmount:  ${OrderInfo[position].OrderAmount} "
           //     userName.text = "UserId : ${OrderInfo[position].userId.toString()} "
             userLocation.text = OrderInfo[position].userLocation.toString()
             userName.text = OrderInfo[position].userName.toString()

                if(OrderInfo[position].isAccept == true){
                    btnAcceptOrder.text="Order Has been Served"
                    btnAcceptOrder.isEnabled = false
                }
                else{
                    btnAcceptOrder.isEnabled = true
                    btnAcceptOrder.text = "Accept Order"
                }
            }

            binding.btnAcceptOrder.setOnClickListener {
                OrderInfo[position].isAccept = true
                notifyItemChanged(position)
                var token = ""
                if (userId.isNotEmpty()) {
                    //fetching user info
                    databaseReference.child(userId)
                        .addValueEventListener(object : ValueEventListener {
                            @SuppressLint("SuspiciousIndentation")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userObj = snapshot.value as Map<String, Object>
                                 userObj.let {
                                     token = userObj["firebasetoken"].toString()

                                     val sendNotificationModel =
                                         SendNotificationModel("Your Order Accepted", "Order Accepted")
                                     val requestNotificaton = RequestNotification()
                                     requestNotificaton.sendNotificationModel = sendNotificationModel
                                     //token is id , whom you want to send notification ,
                                     requestNotificaton.token = token

                                     val apiService = getClient()?.create(FcmApi::class.java)
                                     val responseBodyCall: Call<ResponseBody> =
                                         apiService?.sendNotification(requestNotificaton)!!
                                     responseBodyCall.enqueue(object : Callback<ResponseBody> {
                                         override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                             if (response.isSuccessful) {
                                                 val fcmResponse = response.body()
                                                 // Handle successful response
                                             } else {
                                                 // Handle error response
                                             }
                                         }

                                         override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                             // Handle failure
                                         }
                                     })
                                }
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








