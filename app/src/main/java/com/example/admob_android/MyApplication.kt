package com.example.admob_android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.example.admob_android.activity.OpenAppAdsActivity
import com.example.admob_android.activity.SplashActivity
import com.example.admob_android.service.AppOpenAdManager
import com.google.android.gms.ads.MobileAds

class MyApplication :
    MultiDexApplication(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    companion object {
        private const val TAG = "MyApplication"
    }

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super<MultiDexApplication>.onCreate()
        registerActivityLifecycleCallbacks(this)

        MobileAds.initialize(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
        appOpenAdManager.loadAppOpenAd(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        if (currentActivity is SplashActivity) {
            Log.d(TAG, "Khong hien thi Ads ở Splash Activity")
            return
        }

        Log.d(TAG, "Tải Ads mới từ mọi activity")
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(
                it,
                onShowAdCompleteListener = {
                    Log.d(TAG, "Quảng cáo đã đóng, tải quảng cáo mới.")
                    appOpenAdManager.loadAppOpenAd(this)
                }
            )
        }
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
    }
}


