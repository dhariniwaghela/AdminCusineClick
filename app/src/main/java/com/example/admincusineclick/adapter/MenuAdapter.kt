package com.example.admincusineclick.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincusineclick.databinding.SingleMenuItemBinding
import com.example.admincusineclick.model.ItemDetails
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MenuAdapter(
    private val context: Context)  : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var menuitems: ArrayList<ItemDetails> = ArrayList()

    //firebase instance
    private var menuDatabaseReference: DatabaseReference

    fun updateList(menuItem: MutableList<ItemDetails>) {
        menuitems = menuItem as ArrayList<ItemDetails>
        notifyDataSetChanged()
    }


    init {
        val database = FirebaseDatabase.getInstance()
        menuDatabaseReference = database.reference.child("menu")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding =
            SingleMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuitems.size


    inner class MenuViewHolder(private val binding: SingleMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                menuItemName.text = menuitems[position].itemName.toString()
                menuItemPrice.text = menuitems[position].itemPrice.toString()
                val uriString = menuitems[position].itemImage
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(menuItemImage)

                binding.btnDeleteItem.setOnClickListener {

                    menuDatabaseReference.child(menuitems[position].itemId!!).removeValue()
                        .addOnSuccessListener {
                            notifyItemRemoved(position)
                            menuitems.removeAt(position)
                            Toast.makeText(context,"item Deleted successfully",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Log.d("error", "item not deleted")
                        }
                }


            }
        }
    }
}











