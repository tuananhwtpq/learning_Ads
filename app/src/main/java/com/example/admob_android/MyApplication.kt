package com.example.admob_android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.example.admob_android.activity.SplashActivity
import com.example.admob_android.service.AppOpenAdManager
import com.google.android.gms.ads.MobileAds

class MyApplication :
    MultiDexApplication(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    companion object {
        private const val TAG = "MyApplication"
    }

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super<MultiDexApplication>.onCreate()
        registerActivityLifecycleCallbacks(this)

        MobileAds.initialize(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AppOpenAdManager.loadAppOpenAd(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        if (currentActivity is SplashActivity) {
            Log.d(TAG, "Khong hien thi Ads ở Splash Activity")
            return
        }

        Log.d(TAG, "Tải Ads mới từ mọi activity")

        currentActivity?.let { activity ->
            AppOpenAdManager.showAdIfAvailable(activity, null)
            Log.d(TAG, "Show Ads if it available")
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
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
    }
}


