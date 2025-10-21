package com.example.admob_android

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admob_android.databinding.ActivityRewardAdsBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RewardAdsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RewardAdsActivity"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }

    private lateinit var binding: ActivityRewardAdsBinding
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRewardAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        loadingRewardAds()

        binding.btnAdsReward.setOnClickListener { handleBtnClicked() }

    }

    private fun handleBtnClicked() {

        if (rewardedAd != null) {
            rewardedAd?.show(this, OnUserEarnedRewardListener { rewardItem ->
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d(TAG, "User earned the reward. Amount: $rewardAmount, Type: $rewardType")

            })
        } else {
            loadingRewardAds()
        }
    }

    private fun loadingRewardAds() {

        Log.d(TAG, "loadingRewardAds() is running")

        val adRequest = AdRequest.Builder().build()
        binding.btnAdsReward.isEnabled = false
        binding.btnAdsReward.text = "Reward ads is loading..."

        RewardedAd.load(
            this,
            AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    binding.btnAdsReward.isEnabled = true
                    binding.btnAdsReward.text = "Reward ads is ready. Please try"
                    setFullScreenCallBack()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    binding.btnAdsReward.text = "Fail to load ads. Error: ${error.message}"
                }
            }

        )

    }

    private fun setFullScreenCallBack() {

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdClicked() {
                Log.d(TAG, "Ads was clicked")
            }

            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                loadingRewardAds()
                Log.d(TAG, "Ads was dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                rewardedAd = null

                Log.d(TAG, "Ads failed to show full screen")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ads show full screen content")
            }
        }
    }
}