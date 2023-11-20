package com.example.admincusineclick.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincusineclick.databinding.SingleMenuItemBinding
import com.example.admincusineclick.model.ItemDetails
import com.google.firebase.database.DatabaseReference

class MenuAdapter(
    private val context: Context,
    private val menuList: ArrayList<ItemDetails>,
    databaseReference: DatabaseReference
)  :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = SingleMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int= menuList.size


   inner class MenuViewHolder(private val binding: SingleMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuList[position]
                val imageUri = menuItem.itemImage
                val uri = Uri.parse(imageUri)
                Glide.with(context).load(uri).into(menuItemImage)
                menuItemName.text = menuItem.itemName
                menuItemPrice.text = menuItem.itemPrice

            }

        }
    }
    }








