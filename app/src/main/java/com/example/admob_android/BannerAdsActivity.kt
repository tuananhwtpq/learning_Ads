package com.example.admob_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admob_android.databinding.ActivityBannerAdsBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BannerAdsActivity : AppCompatActivity() {

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"
    }

    private lateinit var binding: ActivityBannerAdsBinding
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBannerAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        loadBanner()
    }

    private fun loadBanner() {
        val adView = AdView(this)
        adView.apply {
            adUnitId = AD_UNIT_ID
            setAdSize(getAdsSize())
        }

        this.adView = adView

        binding.adViewContainer.removeAllViews()
        binding.adViewContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun getAdsSize(): AdSize {
        val displayMetrics = resources.displayMetrics
        val adWidthPixels =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val windowMetrics = windowManager.currentWindowMetrics
                val insets = windowMetrics.windowInsets.getInsets(
                    android.view.WindowInsets.Type.systemBars() or android.view.WindowInsets.Type.displayCutout()
                )
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                @Suppress("DEPRECATION")
                displayMetrics.widthPixels
            }

        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }
}