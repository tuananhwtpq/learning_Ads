package com.example.admob_android

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.admob_android.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result
                Log.d(TAG, "Token: $token")
            })

        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()

            Log.d(TAG, "FCM can't post notifications without POST_NOTIFICATIONS permission")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        binding.btnAdBanner.setOnClickListener { navigateToActivity(BannerAdsActivity::class.java) }
        binding.btnAddInterstital.setOnClickListener { navigateToActivity(InterstitialAdsActivity::class.java) }
        binding.btnAdsReward.setOnClickListener { navigateToActivity(RewardAdsActivity::class.java) }
        binding.btnAdsNative.setOnClickListener { navigateToActivity(NativeAdsActivity::class.java) }
        binding.btnCrashApp.setOnClickListener {
            throw RuntimeException("Test Crash")
            Firebase.crashlytics.log("message")

        }
        createNotificationChannel()
        handleRemoteConfig()

        binding.btnFcmMessaging.setOnClickListener { askNotificationPermission() }
        //Firebase.messaging.isAutoInitEnabled = true

        binding.btnChangeConfig.setOnClickListener { navigateToActivity(TestRemoteConfigActivity::class.java) }


    }

    private fun handleRemoteConfig() {

        //val remoteConfig = Firebase.remoteConfig
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result
                })

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun navigateToActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Messages"
            val channelName = "Messages"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "All messages."

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.d(TAG, "Notification Channel đã được tạo.")
        }
    }
}