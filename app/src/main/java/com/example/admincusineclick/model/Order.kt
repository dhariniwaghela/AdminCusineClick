package com.example.admincusineclick.model

data class Order(
    var userId:String?=null,
    var userName: String? =null,
    var userLocation: String?=null,
    var OrderAmount: String? = null,
    var itemList: List<String>?= null
)
