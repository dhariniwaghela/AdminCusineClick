package com.admincusineclick.model

data class Order(
    var userId:String?=null,
    var userName: String? =null,
    var userLocation: String?=null,
    var OrderAmount: String? = null,
    var isAccept:Boolean?= false
)
