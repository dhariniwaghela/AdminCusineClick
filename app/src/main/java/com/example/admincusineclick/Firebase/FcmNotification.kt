package com.example.admincusineclick.Firebase

import com.google.gson.annotations.SerializedName


class RequestNotification {
    @SerializedName("to") //  "to" changed to token
    var token: String? = null

    @SerializedName("notification")
    var sendNotificationModel: SendNotificationModel? = null
}

class SendNotificationModel(var body: String, var title: String)
