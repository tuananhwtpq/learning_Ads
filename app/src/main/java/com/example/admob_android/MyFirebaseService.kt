package com.example.admob_android

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseService"

        private const val CHANNEL_ID = "Messages"
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")

        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Nhận notification title: ${notification.title}")
            Log.d(TAG, "Nhận notification message: ${notification.body}")

            val title = notification.title ?: "Thông báo mới"
            val body = notification.body ?: "Bạn có tin nhắn mới."

            sendCustomNotification(title, body)
            return
        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Nhận data message: " + remoteMessage.data)

            val title = remoteMessage.data["title"] ?: "New message"
            val body = remoteMessage.data["text"]

            if (body != null) {
                sendCustomNotification(title, body)
            } else {
                Log.w(TAG, "Data message không chứa key 'text'.")
            }
        }
    }

    private fun sendCustomNotification(title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            Log.w(TAG, "Không thể gửi thông báo. App chưa được cấp quyền POST_NOTIFICATIONS.")
            return
        }

        val manager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}