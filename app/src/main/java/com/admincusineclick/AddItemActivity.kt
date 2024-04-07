package com.admincusineclick

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.admincusineclick.databinding.ActivityAddItemBinding
import com.admincusineclick.model.ItemDetails
import com.admincusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class AddItemActivity : AppCompatActivity() {

    //food item detail
    private lateinit var foodItemName: String
    private lateinit var foodItemPrice: String
    private lateinit var foodItemDescription: String
    private lateinit var foodItemIngredients: String
    private lateinit var foodItemCategory: String
    private var foodItemImageUri: Uri? = null
    private lateinit var foodItemCalories: String
    private var restaurantName : String = null.toString()
    private var restaurantId : String = null.toString()

    lateinit var radioButton: RadioButton

    //firebase real time database
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    //Firebase object for fetching restaurant name
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo : UserModel


    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialization
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //fetch Restaurant name in menu by default
        binding.tvRestaurantName.text = restaurantName

        binding.selectimage.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.foodCategory.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val selectedOption: Int = binding.foodCategory!!.checkedRadioButtonId
            // Assigning id of the checked radio button
            radioButton = findViewById(selectedOption)

        })
        binding.btnSave.setOnClickListener {
            //get data from fields
            foodItemName = binding.etFoodItemName.text.toString().trim()
            foodItemDescription = binding.etDescription.text.toString().trim()
            foodItemPrice = binding.etFoodItemPrice.text.toString().trim()
            foodItemCategory = radioButton.text.toString()
            foodItemIngredients = binding.etIngredients.text.toString().trim()
            foodItemCalories = binding.etCalories.text.toString().trim()


            if (!(foodItemName.isBlank() || foodItemDescription.isBlank() || foodItemPrice.isBlank() || foodItemCategory.isBlank() || foodItemIngredients.isBlank() || foodItemCalories.isBlank())) {
                restaurantName?.let {
                    uploadData()
                } ?: run {
                    Toast.makeText(this, "Failed to get restaurant name", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "fill all details", Toast.LENGTH_SHORT).show()
            }

        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        //get a refernce data node"menu"
        val Menuref: DatabaseReference = database.getReference("menu").push()
        //generate a unique key for each menu item
        val newItemKey: String? = Menuref.key
        if (foodItemImageUri != null) {
            val StorageRef: StorageReference = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = StorageRef.child("Menu_image/${newItemKey}.jpg")
            val uploadTask: UploadTask = imageRef.putFile(foodItemImageUri!!)
            uploadTask.addOnCompleteListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    //create new menu item
                    val newItem = ItemDetails(
                        itemName = foodItemName,
                        itemDescriptions = foodItemDescription,
                        itemIngredients = foodItemIngredients,
                        itemCategory = foodItemCategory,
                        itemCalories = foodItemCalories,
                        itemImage = downloadUrl.toString(),
                        itemPrice = foodItemPrice,
                        itemId = newItemKey,
                        restaurantName = restaurantName,
                        restaurantId = restaurantId
                    )

                    Menuref.setValue(newItem).addOnSuccessListener {
                        Toast.makeText(this, "Item Added Successfully in Menu", Toast.LENGTH_SHORT)
                            .show()
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to upload data", Toast.LENGTH_SHORT).show()
                    }

                }

            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image ", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show()

        }

    }

    private fun getRestaurantInfo(callback: (String?, String?) -> Unit) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            databaseReference =
                FirebaseDatabase.getInstance().getReference("Admin").child("AdminData")
                    .child(userId)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userinfo = snapshot.getValue(UserModel::class.java)!!
                    val fetchedRestaurantName = userinfo.restaurantName
                    val fetchedRestaurantId = userinfo.restaurantId
                    callback(fetchedRestaurantName, fetchedRestaurantId)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, null)
                }
            })
        } else {
            callback(null, null)
        }
    }

    override fun onResume() {
        super.onResume()

        // Fetch the restaurant information when the activity is resumed
        getRestaurantInfo { fetchedRestaurantName, fetchedRestaurantId ->
            if (fetchedRestaurantName != null && fetchedRestaurantId != null) {
                restaurantName = fetchedRestaurantName
                restaurantId = fetchedRestaurantId
            } else {
                Log.e("AddItemActivity", "Failed to fetch restaurant information.")
            }
            binding.tvRestaurantName.text = restaurantName
        }
    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectimage.setImageURI(uri)
            foodItemImageUri = uri
        }
    }
}