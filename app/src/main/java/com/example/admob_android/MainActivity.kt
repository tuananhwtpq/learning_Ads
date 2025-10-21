package com.example.admob_android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admob_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdBanner.setOnClickListener { navigateToActivity(BannerAdsActivity()) }
        binding.btnAddInterstital.setOnClickListener { navigateToActivity(InterstitialAdsActivity()) }
        binding.btnAdsReward.setOnClickListener { navigateToActivity(RewardAdsActivity()) }
        binding.btnAdsNative.setOnClickListener { navigateToActivity(NativeAdsActivity()) }

    }

    private fun navigateToActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}