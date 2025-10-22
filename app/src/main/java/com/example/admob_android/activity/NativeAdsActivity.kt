package com.example.admob_android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.admob_android.databinding.ActivityNativeAdsBinding
import com.example.admob_android.databinding.AdUnifiedBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class NativeAdsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NativeAdsActivity"
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    }

    private lateinit var binding: ActivityNativeAdsBinding

    private var nativeAdView: NativeAdView? = null

    //region ONCREATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        loadNativeAds()

        binding.btnRetry.setOnClickListener { handleButtonClicked() }

    }

    private fun handleButtonClicked() {
        nativeAdView?.destroy()
        nativeAdView = null
        loadNativeAds()
    }

    //region LOAD ADS
    @SuppressLint("SetTextI18n")
    private fun loadNativeAds() {

        binding.btnRetry.isEnabled = false
        binding.btnRetry.text = "Ads is loading..."

        val adLoader = AdLoader.Builder(this, AD_UNIT_ID)
            .withNativeAdOptions(
                NativeAdOptions.Builder().setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG, "Ad was clicked")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.d(TAG, "Ad was closed")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "Ad was fail to load. Error: ${p0.message}")
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Log.d(TAG, "Ad impression")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d(TAG, "Ad was loaded")
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.d(TAG, "As was opened")
                }

                override fun onAdSwipeGestureClicked() {
                    super.onAdSwipeGestureClicked()
                    Log.d(TAG, "Ad was swiped")
                }

            })
            //region FOR NATIVE AD
            .forNativeAd { nativeAd: NativeAd ->

                binding.adViewContainer.removeAllViews()

                val nativeBinding = AdUnifiedBinding.inflate(layoutInflater)
                val nativeAdView = nativeBinding.root
                this.nativeAdView = nativeAdView

                nativeAdView.headlineView = nativeBinding.adHeadline
                nativeBinding.adHeadline.text = nativeAd.headline


                nativeAdView.starRatingView = nativeBinding.adStars
                if (nativeAd.starRating != null) {
                    nativeBinding.adStars.visibility = View.VISIBLE
                    nativeBinding.adStars.rating = nativeAd.starRating!!.toFloat()
                } else {
                    nativeBinding.adStars.visibility = View.INVISIBLE
                }
                //region MEDIA CONTENT
                nativeAdView.mediaView = nativeBinding.adMedia

                val mediaContent = nativeAd.mediaContent
                val videoController = mediaContent?.videoController

                if (mediaContent != null) {
                    nativeBinding.adMedia.setMediaContent(mediaContent)
                    nativeBinding.adMedia.visibility = View.VISIBLE
                } else {
                    nativeBinding.adMedia.visibility = View.GONE
                }


                if (videoController != null && mediaContent.hasVideoContent()) {
                    videoController.videoLifecycleCallbacks =
                        object : VideoController.VideoLifecycleCallbacks() {
                            override fun onVideoEnd() {
                                super.onVideoEnd()
                            }

                            override fun onVideoPlay() {
                                super.onVideoPlay()
                            }

                            override fun onVideoStart() {
                                super.onVideoStart()
                            }
                        }

                    Log.d(TAG, "Load Video success")
                } else {
                    Log.d(TAG, "Video content is Image, cannot set video callback")
                }

                nativeAdView.bodyView = nativeBinding.adBody
                if (nativeAd.body != null) {
                    nativeBinding.adBody.visibility = View.VISIBLE
                    nativeBinding.adBody.text = nativeAd.body
                } else {
                    nativeBinding.adBody.visibility = View.INVISIBLE
                }

                nativeAdView.advertiserView = nativeBinding.adBadge
                if (nativeAd.advertiser != null) {
                    nativeBinding.adBadge.visibility = View.VISIBLE
                    nativeBinding.adBadge.text = nativeAd.advertiser
                    Log.d(TAG, "Ad advertoser is ready")

                } else {
                    nativeBinding.adBadge.visibility = View.VISIBLE
                    nativeBinding.adBadge.text = "Ad Advertiser"
                    Log.d(TAG, "Ad advertoser is null")
                }

                nativeAdView.callToActionView = nativeBinding.btnCallToAction
                if (nativeAd.callToAction != null) {
                    nativeBinding.btnCallToAction.visibility = View.VISIBLE
                    nativeBinding.btnCallToAction.text = nativeAd.callToAction
                } else {
                    nativeBinding.btnCallToAction.visibility = View.INVISIBLE
                }

                nativeAdView.iconView = nativeBinding.adAppIcon
                if (nativeAd.icon != null) {
                    nativeBinding.adAppIcon.visibility = View.VISIBLE
                    nativeBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
                } else {
                    nativeBinding.adAppIcon.visibility = View.GONE
                }

                nativeAdView.storeView = nativeBinding.adStore
                if (nativeAd.store != null) {
                    nativeBinding.adStore.visibility = View.VISIBLE
                    nativeBinding.adStore.text = nativeAd.store
                } else {
                    nativeBinding.adStore.visibility = View.INVISIBLE
                }

                nativeAdView.priceView = nativeBinding.adPrice
                if (nativeAd.price != null) {
                    nativeBinding.adPrice.visibility = View.VISIBLE
                    nativeBinding.adPrice.text = nativeAd.price
                } else {
                    nativeBinding.adPrice.visibility = View.INVISIBLE
                }

                nativeAdView.setNativeAd(nativeAd)
                binding.adViewContainer.addView(nativeAdView)


            }
            .build()

        //region AD REQUEST
        val adRequest = AdRequest.Builder().build()
        adLoader.loadAd(adRequest)

        binding.btnRetry.isEnabled = true
        binding.btnRetry.text = "Retry"
    }
}