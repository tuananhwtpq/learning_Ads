package com.example.admob_android.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage

class MyNewService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyNewService"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        //handle FCM

        //check message contain data payload
        if (message.data.isNotEmpty()) {

        }

        //check message contain a notification
        message.notification?.let {
            Log.d(TAG, "Message notification: ${it.body}")
        }

    }

    private fun sendRegistrationToServer(token: String) {
    }

}