package com.example.admob_android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.admob_android.databinding.ActivityInterstitialAdsBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdsActivity : AppCompatActivity() {

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val TAG = "InterstitialAdsActivity"
    }

    private lateinit var binding: ActivityInterstitialAdsBinding
    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterstitialAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        loadInterstitialAd()

        binding.btnRetry.setOnClickListener { showInterstitialAds() }
    }

    @SuppressLint("SetTextI18n")
    private fun loadInterstitialAd() {
        Log.d(TAG, "LoadInterstitialAd() is running")

        val adRequest = AdRequest.Builder().build()
        binding.btnRetry.isEnabled = false
        binding.btnRetry.text = "Ads is loading..."

        InterstitialAd.load(
            this,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    binding.btnRetry.isEnabled = true
                    binding.btnRetry.text = "Ads is ready. PLease try"
                    setFullScreenCallBack()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    binding.btnRetry.text = "Fail to load ads. Error: ${error.message}"
                }
            }
        )
    }

    private fun setFullScreenCallBack() {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "Ads was clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitialAd()
                Log.d(TAG, "Ads was dismissed full screen")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                interstitialAd = null
                Log.d(TAG, "Ads failed to show full screen")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ads was shown full screen")

            }
        }
    }

    private fun showInterstitialAds() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
        } else {
            loadInterstitialAd()
        }
    }
}