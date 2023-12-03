package com.example.admincusineclick.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincusineclick.databinding.OrderInfoSingleItemBinding
import com.example.admincusineclick.model.ItemDetails
import com.example.admincusineclick.model.Order
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderAdapter(private val context: Context)  :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var OrderInfo: ArrayList<Order> = ArrayList()

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
            binding.apply {
                orderFoodPrice.text = OrderInfo[position].OrderAmount.toString()
            }

        }
    }
}








