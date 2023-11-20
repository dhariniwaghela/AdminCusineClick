package com.example.admincusineclick

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.admincusineclick.databinding.ActivityAddOfferBannerBinding
import com.example.admincusineclick.model.ItemDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddOfferBannerActivity : AppCompatActivity() {
    private val binding: ActivityAddOfferBannerBinding by lazy {
        ActivityAddOfferBannerBinding.inflate(layoutInflater)
    }
    private var BannerItemImageUri: Uri? = null
    private lateinit var bannerDescription: String

    //firebase real time database
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialization
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.bannerimage.setOnClickListener{
            pickImage.launch("image/*")
        }
        binding.btnSave.setOnClickListener{
            //get data from fields
            bannerDescription = binding.etOfferDescription.text.toString().trim()
            if (!bannerDescription.isBlank()) {
                uploadData()
            } else {
                Toast.makeText(this, "fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun uploadData() {
        //get a refernce data node"Banner"
        val bannerRef: DatabaseReference = database.getReference("Banner")
        //generate a unique key for each Banner item
        val newItemKey: String? = bannerRef.push().key

        if (BannerItemImageUri != null) {
            val StorageRef: StorageReference = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = StorageRef.child("Banner_image/${newItemKey}.jpg")
            val uploadTask: UploadTask = imageRef.putFile(BannerItemImageUri!!)
            uploadTask.addOnCompleteListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    //create new Banner item
                    val newItem = ItemDetails(
                        itemDescriptions = bannerDescription,
                        itemImage = downloadUrl.toString(),
                    )
                    newItemKey?.let { key ->
                        bannerRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "data uploaded successfully", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                            .addOnCanceledListener {
                                Toast.makeText(this, "Failed to upload data", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image ", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show()

        }

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.bannerimage.setImageURI(uri)
            BannerItemImageUri = uri
        }
    }


}