package com.admincusineclick.model


data class UserModel(
        val restaurantId : String? = null,
        var name: String? =null,
        val email: String? =null,
        val password: String? =null,
        var restaurantName: String? = null,
        val imgUri:String?=null,
        val firebasetoken:String?=null,
        val location:String?=null
    )
