package com.example.admob_android.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.admob_android.databinding.ActivityOpenAppAdsBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd

class OpenAppAdsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "OpenAppAdsActivity"

        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
    }

    private lateinit var binding: ActivityOpenAppAdsBinding
    private var appOpenAd: AppOpenAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenAppAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        loadingAds()

    }

    private fun loadingAds() {

        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(
            this,
            AD_UNIT_ID,
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad

                    setFullScreenCallBack()
                }
            }
        )
    }

    private fun setFullScreenCallBack() {

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "Ads was clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.d(TAG, "Ads was dismissed to full screen")
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                super.onAdFailedToShowFullScreenContent(error)
                Log.d(TAG, "Ads was failed to show full screen. Error: ${error.message}")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "Ads was impressed")
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.d(TAG, "Ads was showed full screen")
            }
        }
    }
}