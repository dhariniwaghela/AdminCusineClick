package com.example.admincusineclick.notificationApi

data class FcmNotification(
    val to: String, // Device token
    val data: Map<String, String> // Notification data
)

data class FcmResponse(
    val success: Int,
    val failure: Int
)