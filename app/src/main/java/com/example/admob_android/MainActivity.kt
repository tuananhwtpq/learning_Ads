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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.admob_android.activity.BannerAdsActivity
import com.example.admob_android.activity.InterstitialAdsActivity
import com.example.admob_android.activity.NativeAdsActivity
import com.example.admob_android.activity.RewardAdsActivity
import com.example.admob_android.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging

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
                Toast.LENGTH_SHORT,
            ).show()

            Log.d(TAG, "FCM can't post notifications without POST_NOTIFICATIONS permission")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        handleButtonOnClicked()
        createNotificationChannel()
        handleRemoteConfig()

    }

    private fun handleButtonOnClicked() {
        binding.btnAdBanner.setOnClickListener { navigateToActivity(BannerAdsActivity::class.java) }
        binding.btnAddInterstital.setOnClickListener { navigateToActivity(InterstitialAdsActivity::class.java) }
        binding.btnAdsReward.setOnClickListener { navigateToActivity(RewardAdsActivity::class.java) }
        binding.btnAdsNative.setOnClickListener { navigateToActivity(NativeAdsActivity::class.java) }
        binding.btnCrashApp.setOnClickListener {
            throw RuntimeException("Test Crash")
            Firebase.crashlytics.log("message")

        }
        binding.btnFcmMessaging.setOnClickListener { askNotificationPermission() }
        binding.btnChangeConfig.setOnClickListener { navigateToActivity(TestRemoteConfigActivity::class.java) }

    }

    private fun handleRemoteConfig() {

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Has permission!")
                fetchFcmToken()

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

                AlertDialog.Builder(this)
                    .setTitle("You need to access notification permission to use this")
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()

            } else {
                Log.d(TAG, "Is loading permission")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            fetchFcmToken()
        }
    }

    /**
     * Su dung de xin quyen cac may co API nho hon 26
     */
    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "Token: $token")
        })
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