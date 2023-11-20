package com.example.admincusineclick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincusineclick.databinding.OrderInfoSingleItemBinding

class OrderAdapter(val orderitemName:List<String>,val orderitemPrice: List<String>, val orderitemImage: List<Int>)  :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.OrderViewHolder {
        val binding = OrderInfoSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int= orderitemName.size


    inner class OrderViewHolder(private val binding: OrderInfoSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                orderFoodName.text = orderitemName[position]
                orderFoodPrice.text = orderitemPrice[position]
                orderFoodImage.setImageResource(orderitemImage[position])
            }

        }
    }
}








