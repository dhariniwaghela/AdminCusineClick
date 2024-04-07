package com.admincusineclick.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.admincusineclick.databinding.SingleOfferBannerBinding
import com.admincusineclick.model.BannerDetails
import com.google.firebase.database.DatabaseReference

class OfferAdapter(
    private val context: Context,
    private val bannerList: ArrayList<BannerDetails>,
    databaseReference: DatabaseReference) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferAdapter.OfferViewHolder {
        val binding = SingleOfferBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun getItemCount(): Int = bannerList.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OfferViewHolder(private val binding: SingleOfferBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val bannerItem = bannerList[position]
                val imageUri = bannerItem.bannerImage
                val uri = Uri.parse(imageUri)
                Glide.with(context).load(uri).into(bannerImage)
                tvOfferDescription.text = bannerItem.bannerDescription

            }
        }
    }
}









