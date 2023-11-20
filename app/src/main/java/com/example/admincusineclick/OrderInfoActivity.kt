package com.example.admincusineclick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincusineclick.adapter.OrderAdapter
import com.example.admincusineclick.databinding.ActivityOrderInfoBinding

class OrderInfoActivity : AppCompatActivity() {
    private val binding: ActivityOrderInfoBinding by lazy {
        ActivityOrderInfoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val OrderitemName = listOf("Burger","Sandwhich","Momo")
        val OrderitemtemPrice = listOf("$5","$7","$10")
        val OrderitemImage = listOf(
            R.drawable.menu1,
            R.drawable.menu5,
            R.drawable.menu6
        )
        val adapter = OrderAdapter(ArrayList(OrderitemName) , ArrayList(OrderitemtemPrice) , ArrayList(OrderitemImage))
        binding.recyclerviewOrderitem.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewOrderitem.adapter = adapter
    }
}