package com.example.admob_android.service

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.admob_android.OnShowAdCompleteListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date
import kotlin.contracts.contract

class AppOpenAdManager {

    companion object {
        private const val TAG = "AppOpenAdManager"

        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
        private const val AD_EXPIRATION_HOURS = 4L
    }

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd: Boolean = false
    private var isShowingAd: Boolean = false

    private var loadTime: Long = 0

    private val isAdAvailable: Boolean
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(AD_EXPIRATION_HOURS)

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    fun loadAppOpenAd(context: Context) {

        if (isAdAvailable || isLoadingAd) return

        isLoadingAd = true

        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d(TAG, "App Open Ads load fail. Error: ${p0.message}")
                    isLoadingAd = false

                }

                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }
            }
        )
    }

    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        if (isShowingAd) {
            Log.d(TAG, "Ad is showing")
            return
        }

        if (!isAdAvailable) {
            Log.d(TAG, "Ads is not ready yet")
            onShowAdCompleteListener.onShowAdComplete()
            return
        }

        Log.d(TAG, "Ads is going to show")
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "ADs was dismissed")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener.onShowAdComplete()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(TAG, "Ads was failed to show: ${adError.message}")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener.onShowAdComplete()
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ads show success")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ads was clicked")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ads was impressed")
            }
        }

        isShowingAd = true
        appOpenAd?.show(activity)
    }


}